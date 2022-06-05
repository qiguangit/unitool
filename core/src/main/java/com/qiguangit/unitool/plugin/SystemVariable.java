package com.qiguangit.unitool.plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SystemVariable {

    private static final Map<String, String> systemVariables = new HashMap<>();
    public static final String KEY_PROJECT_PATH = "projectPath";
    public static final String KEY_USER_HOME = "userHome";
    public static final String KEY_TEMPLATE_PATH = "templatePath";

    static {
        loadSystemVariable();
    }

    private SystemVariable() {

    }

    private static void loadSystemVariable() {

        systemVariables.put(KEY_PROJECT_PATH, System.getProperty("user.dir"));
        systemVariables.put(KEY_TEMPLATE_PATH, System.getProperty("user.dir") + File.separator + "template");
        File unitool = new File(System.getProperty("user.home") + File.separator + ".unitool");
        if (!unitool.exists()) {
            unitool.mkdirs();
        }
        systemVariables.put(KEY_USER_HOME, System.getProperty("user.home") + File.separator + ".unitool");
    }

    public static String get(String key) {
        return get(key, "");
    }

    public static String get(String key, String defaultValue) {
        if (systemVariables.containsKey(key)) {
            return systemVariables.get(key);
        }
        return defaultValue;
    }

    public static void put(String key, String value) {
        systemVariables.put(key, value);
    }

}
