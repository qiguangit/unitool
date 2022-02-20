package com.qiguangit.unitool.plugin.generate.manager;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;

public class FreeMarkerConfigurationManager {

    private static volatile Configuration configuration;

    private static final String TEMPLATE = "/resources/template";

    private FreeMarkerConfigurationManager() {
    }

    private static void initConfiguration() {
        configuration = new Configuration(Configuration.VERSION_2_3_22);
        final String resourcePath = FreeMarkerConfigurationManager.class
                .getResource(TEMPLATE).getFile();
        try {
            configuration.setDirectoryForTemplateLoading(new File(resourcePath));
            configuration.setDefaultEncoding("UTF-8");
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        } catch (IOException e) {
            throw new RuntimeException(String.format("folder[%s] no found", TEMPLATE));
        }
    }

    public static Configuration getInstance() {
        if (configuration == null) {
            synchronized (FreeMarkerConfigurationManager.class) {
                if (configuration == null) {
                    initConfiguration();
                }
            }
        }
        return configuration;
    }

}
