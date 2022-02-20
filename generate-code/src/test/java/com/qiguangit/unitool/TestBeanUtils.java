package com.qiguangit.unitool;

import com.qiguangit.unitool.plugin.generate.model.HibernateModel;
import com.qiguangit.unitool.util.BeanUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestBeanUtils {
    @Test
    public void testBean2Map() {
        HibernateModel.Field field = new HibernateModel.Field();
        field.setFieldName("aaa");
        field.setAttrName("bbbb");
        field.setLength("1");
        field.setDefaultValue("aaa");
        final Map<String, Object> map = BeanUtils.bean2Map(field);
        System.out.println(map);
    }

    @Test
    public void testMap2Bean() {
        Map<String, Object> map = new HashMap<>();
        map.put("fieldName", "aaa");
        map.put("attrName", "bbbb");
        map.put("length", 1);
        map.put("defaultValue", "aaa");
        final HibernateModel.Field field = BeanUtils.map2Bean(map, HibernateModel.Field.class);
        System.out.println(field);
    }
}
