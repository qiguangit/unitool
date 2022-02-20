package com.qiguangit.unitool.plugin.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class PluginJarInfo implements Serializable {

    private String id;
    private String name;
    private String description;
    private String version;
    private List<FxConfiguration> fxConfigurations;

    @Data
    @Builder
    public static class FxConfiguration implements Serializable {
        private String fxmlPath;
        private String resourceBundleName;
        private String title;
        private String menu;
        private String controllerType;
    }
}
