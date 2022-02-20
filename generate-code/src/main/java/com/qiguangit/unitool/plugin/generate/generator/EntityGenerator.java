package com.qiguangit.unitool.plugin.generate.generator;

import com.qiguangit.unitool.plugin.generate.manager.FreeMarkerConfigurationManager;
import com.qiguangit.unitool.util.CamelCase;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import com.qiguangit.unitool.plugin.generate.model.HibernateModel;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class EntityGenerator {

    private static final String CLASS_NAME = "className";
    private static final String TABLE_NAME = "tableName";
    private static final String FIELDS = "fields";
    private static final String PACKAGE_NAME = "packageName";
    private static final String ENTITY_NAME = "entityName";

    private static volatile EntityGenerator generator;

    private EntityGenerator() {
    }

    public static EntityGenerator getInstance() {
        if (generator == null) {
            synchronized (HibernateTemplateGenerator.class) {
                if (generator == null) {
                    generator = new EntityGenerator();
                }
            }
        }
        return generator;
    }

    public void generateEntity(HibernateModel model, Writer writer) throws IOException, TemplateException {
        final Configuration conf = FreeMarkerConfigurationManager.getInstance();
        Map<String, Object> root = new HashMap<>();
        root.put("convertStr", new CamelCase());
        root.put(PACKAGE_NAME, model.getPackageName());
        root.put(ENTITY_NAME, model.getClassName());
        root.put(CLASS_NAME, model.getClassName());
        root.put(TABLE_NAME, model.getTableName());
        root.put(FIELDS, model.getFields());

        final Template template = conf.getTemplate("/entity.ftl");
        template.process(root, writer);
    }

}
