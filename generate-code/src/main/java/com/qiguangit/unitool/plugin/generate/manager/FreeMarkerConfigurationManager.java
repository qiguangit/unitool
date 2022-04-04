package com.qiguangit.unitool.plugin.generate.manager;

import com.qiguangit.unitool.plugin.SystemVariable;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;

public class FreeMarkerConfigurationManager {

    private static volatile Configuration configuration;

    private static final String TEMPLATE = SystemVariable.get(SystemVariable.KEY_PROJECT_PATH) + File.separator + "template";

    private FreeMarkerConfigurationManager() {
    }

    private static void initConfiguration() {
        configuration = new Configuration(Configuration.VERSION_2_3_22);
        File file = new File(TEMPLATE);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            configuration.setDirectoryForTemplateLoading(file);
            configuration.setDefaultEncoding("UTF-8");
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        } catch (IOException e) {
            throw new RuntimeException(String.format("folder[%s] not found", TEMPLATE));
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
