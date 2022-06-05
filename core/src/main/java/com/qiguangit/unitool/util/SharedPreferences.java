package com.qiguangit.unitool.util;

import com.qiguangit.unitool.plugin.SystemVariable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

public class SharedPreferences {
    private final File spFile;
    private Map<String, Object> spMap;
    private volatile boolean loading;
    private final Object loadingLock = new Object();
    private final Object writingToDiskLock = new Object();
    private static final Map<String, SharedPreferences> sharedPrefCaches = new HashMap<>();

    public static synchronized SharedPreferences getSharedPreferences(String fileName) {
        SharedPreferences sp = sharedPrefCaches.get(fileName);
        if (sp == null) {
            sp = new SharedPreferences(fileName);
            sharedPrefCaches.put(fileName, sp);
        }
        return sp;
    }

    private SharedPreferences(String fileName) {
        spFile = new File(SystemVariable.get(SystemVariable.KEY_PROJECT_PATH), fileName + ".xml");
        loadMapFromDisk();
    }

    private void loadMapFromDisk() {
        if (loading) {
            return;
        }
        loading = true;
        new Thread(()->{
            synchronized (loadingLock) {
                try {
                    spMap = XmlUtils.readMapXml(spFile);
                } finally {
                    loading = false;
                    loadingLock.notifyAll();
                }
            }
        }).start();
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String defValue) {
        awaitLoadingLock();
        String value = (String) spMap.get(key);
        return value == null ? defValue : value;
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defValue) {
        awaitLoadingLock();
        Integer value = (Integer) spMap.get(key);
        return value == null ? defValue : value;
    }

    public float getFloat(String key) {
        return getFloat(key, 0.0F);
    }

    public float getFloat(String key, float defValue) {
        awaitLoadingLock();
        Float value = (Float) spMap.get(key);
        return value == null ? defValue : value;
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defValue) {
        awaitLoadingLock();
        Boolean value = (Boolean) spMap.get(key);
        return value == null ? defValue : value;
    }

    public boolean containsKey(String key) {
        return spMap.containsKey(key);
    }

    public Map<String, Object> getAll() {
        awaitLoadingLock();
        return new HashMap<>(spMap);
    }

    private void awaitLoadingLock() {
        synchronized (loadingLock) {
            while (loading) {
                try {
                    loadingLock.wait();
                } catch (InterruptedException e) {

                }
            }
        }

    }

    public Editor edit() {
        return new Editor();
    }

    public final class Editor {
        private final Map<String, Object> modifiedMap = new ConcurrentHashMap<>();
        private boolean clearFlag;

        public Editor putString(String key, String value) {
            modifiedMap.put(key, value == null ? "" : value);
            return this;
        }

        public Editor putInt(String key, int value) {
            modifiedMap.put(key, value);
            return this;
        }

        public Editor putFloat(String key, float value) {
            modifiedMap.put(key, value);
            return this;
        }

        public Editor putLong(String key, long value) {
            modifiedMap.put(key, value);
            return this;
        }

        public Editor putBoolean(String key, boolean value) {
            modifiedMap.put(key, value);
            return this;
        }

        public Editor putAll(Map<String, ?> map) {
            modifiedMap.putAll(map);
            return this;
        }

        public Editor remove(String key) {
            modifiedMap.put(key, this);
            return this;
        }

        public Editor clear() {
            clearFlag = true;
            return this;
        }

        public boolean commit() {
            MemoryCommitResult result = commitToMemory();
            if (!result.changesMade) {
                return true;
            }
            enqueueDiskWrite(result);
            try {
                result.writtenToDiskLatch.await();
            } catch (InterruptedException e) {
                return false;
            }
            return result.writeToDiskResult;
        }

        private MemoryCommitResult commitToMemory() {
            if (loading) {
                synchronized (loadingLock) {
                    while (loading) {
                        try {
                            loadingLock.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
            MemoryCommitResult result = new MemoryCommitResult();
            result.mapToWriteToDisk = spMap;
            synchronized (this) {
                if (clearFlag) {
                    spMap.clear();
                    clearFlag = false;
                    result.changesMade = true;
                }

                if (!modifiedMap.isEmpty()) {
                    for (Map.Entry<String, Object> map : modifiedMap.entrySet()) {
                        String key = map.getKey();
                        Object value = map.getValue();

                        if (value == null || value == this) {
                            if (!spMap.containsKey(key)) {
                                continue;
                            }
                            spMap.remove(key);
                        } else {
                            if (value.equals(spMap.get(key))) {
                                continue;
                            }
                            spMap.put(key, value);
                        }
                        result.changesMade = true;
                    }
                    modifiedMap.clear();
                }
            }
            return result;
        }

        private void enqueueDiskWrite(MemoryCommitResult result) {
            Executors.newSingleThreadExecutor().execute(()->{
                synchronized (writingToDiskLock) {
                    writeToFile(result);
                }
            });
        }

        private void writeToFile(MemoryCommitResult result) {
            try {
                XmlUtils.writeMapXml(result.mapToWriteToDisk, spFile);
                result.setDiskWriteResult(true);
            } catch (Exception e) {
                result.setDiskWriteResult(false);
            }
        }

    }

    private static class MemoryCommitResult {
        public boolean changesMade;
        public Map<String, Object> mapToWriteToDisk;
        public boolean writeToDiskResult;
        public final CountDownLatch writtenToDiskLatch = new CountDownLatch(1);

        public void setDiskWriteResult(boolean result) {
            writeToDiskResult = result;
            writtenToDiskLatch.countDown();
        }
    }

}
