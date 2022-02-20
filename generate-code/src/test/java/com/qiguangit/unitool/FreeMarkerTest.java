package com.qiguangit.unitool;

import com.qiguangit.unitool.plugin.generate.generator.EntityGenerator;
import com.qiguangit.unitool.plugin.generate.model.HibernateModel;
import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class FreeMarkerTest {

    @Test
    public void testHibernateTemplateGenerator() {
    }

    @Test
    public void testEntityGenerator() {
        HibernateModel model = new HibernateModel();
        model.setPackageName("com.cc");
        model.setClassName("tblUser");
        model.setTableName("tbl_user");
        List<HibernateModel.Field> fields = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            HibernateModel.Field field = new HibernateModel.Field();
            field.setFieldName("field_name"+i);
            field.setType("String");
            fields.add(field);
        }
        model.setFields(fields);
        try {
            EntityGenerator.getInstance().generateEntity(model, new OutputStreamWriter(System.out));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

}
