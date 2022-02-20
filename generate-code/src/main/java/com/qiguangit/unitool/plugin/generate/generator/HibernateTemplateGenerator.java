package com.qiguangit.unitool.plugin.generate.generator;

import com.qiguangit.unitool.plugin.generate.manager.FreeMarkerConfigurationManager;
import com.qiguangit.unitool.plugin.generate.model.HibernateModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class HibernateTemplateGenerator {

    private static final String CLASS_NAME = "className";
    private static final String TABLE_NAME = "tableName";
    private static final String FIELDS = "fields";
    private static final String MAPPING_TEMPLATE = "/hibernate-mapping.ftl";

    private static volatile HibernateTemplateGenerator generator;

    private HibernateTemplateGenerator() {
    }

    public static HibernateTemplateGenerator getInstance() {
        if (generator == null) {
            synchronized (HibernateTemplateGenerator.class) {
                if (generator == null) {
                    generator = new HibernateTemplateGenerator();
                }
            }
        }
        return generator;
    }

    public void generateHbmXml(HibernateModel model, Writer writer) throws IOException, TemplateException {
        final Configuration conf = FreeMarkerConfigurationManager.getInstance();
        Map<String, Object> root = new HashMap<>();
        root.put(CLASS_NAME, model.getClassName());
        root.put(TABLE_NAME, model.getTableName());
        root.put(FIELDS, model.getFields());
        final Template template = conf.getTemplate(MAPPING_TEMPLATE);
        template.process(root, writer);
    }

}
