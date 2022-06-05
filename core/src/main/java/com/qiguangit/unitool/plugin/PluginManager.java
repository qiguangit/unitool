package com.qiguangit.unitool.plugin;

import com.qiguangit.unitool.plugin.model.PluginJarInfo;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PluginManager {
    private static final PluginManager manager = new PluginManager();

    private final Map<String, PluginJarInfo> pluginJarInfos = new ConcurrentHashMap<>();

    public static PluginManager getManager() {
        return manager;
    }

    public void addPlugin(String pluginId, PluginJarInfo info) {
        if (StringUtils.isBlank(pluginId) || info == null) {
            return;
        }
        pluginJarInfos.putIfAbsent(pluginId, info);
    }

    public PluginJarInfo getPluginJarInfo(String pluginId) {
        return pluginJarInfos.get(pluginId);
    }

    public Map<String, PluginJarInfo> getPluginJarInfos() {
        return pluginJarInfos;
    }


    public void parsePluginJars() {
        File pluginJarPath = new File("plugins/");
        if (!pluginJarPath.exists()) {
            pluginJarPath.mkdirs();
            return;
        }
        File[] pluginFiles = pluginJarPath.listFiles(file -> file.getName().toLowerCase().endsWith(".jar"));
        // 解析插件
        for (File pluginFile : pluginFiles) {
            PluginJarInfo pluginJarInfo = PluginParser.parse(pluginFile);
            if (pluginJarInfo != null) {
                addPlugin(pluginJarInfo.getId(), pluginJarInfo);
            }
        }
    }

}
