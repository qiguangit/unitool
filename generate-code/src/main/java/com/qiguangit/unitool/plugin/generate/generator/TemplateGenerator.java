package com.qiguangit.unitool.plugin.generate.generator;

import com.qiguangit.unitool.plugin.generate.manager.FreeMarkerConfigurationManager;
import com.qiguangit.unitool.plugin.generate.util.TypeTransfer;
import com.qiguangit.unitool.plugin.generate.util.VariableUtils;
import com.qiguangit.unitool.util.CamelCase;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class TemplateGenerator {
    private static volatile TemplateGenerator generator;

    private TemplateGenerator() {
    }

    public static TemplateGenerator getInstance() {
        if (generator == null) {
            synchronized (TemplateGenerator.class) {
                if (generator == null) {
                    generator = new TemplateGenerator();
                }
            }
        }
        return generator;
    }

    public TemplateGenerator generate(String model, String templateFileName, String exportPath) throws IOException, TemplateException {
        Configuration conf = FreeMarkerConfigurationManager.getInstance();
        Map<String, Object> map = new HashMap<>();
        map.put("convertStr", new CamelCase());
        map.put("typeTransfer", new TypeTransfer());
        map.put("data", model);
        Template template = conf.getTemplate(templateFileName);
        String realFileName = VariableUtils.parseFileName(templateFileName);
        File exportPathFile = new File(exportPath);
        if (!exportPathFile.exists()) {
            exportPathFile.mkdirs();
        }
        FileWriter writer = new FileWriter(exportPath + "/" + realFileName);
        template.process(map, writer);
        writer.close();
        return this;
    }

    public TemplateGenerator generate(String model, String templateFileName, Writer writer) throws IOException, TemplateException {
        Configuration conf = FreeMarkerConfigurationManager.getInstance();
        Map<String, Object> map = new HashMap<>();
        map.put("convertStr", new CamelCase());
        map.put("typeTransfer", new TypeTransfer());
        map.put("data", model);
        Template template = conf.getTemplate(templateFileName);
        template.process(map, writer);
        return this;
    }

}
