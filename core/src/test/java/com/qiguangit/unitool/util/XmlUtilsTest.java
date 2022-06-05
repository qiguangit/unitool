package com.qiguangit.unitool.util;

import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class XmlUtilsTest {
    @Test
    public void writeMapXmlTest() {
        Map<String, Object> m = new HashMap();
        m.put("str", "str str str 中文");
        m.put("int", 10);
        m.put("long", 1000000L);
        m.put("float", 10.2);
        m.put("bool", true);
        File file = new File("G:\\temp\\sharedPreferences.xml");
        XmlUtils.writeMapXml(m, file);
    }

    @Test
    public void readMapXmlTest() {
        File file = new File("G:\\temp\\sharedPreferences.xml");
        Map<String, Object> map = XmlUtils.readMapXml(file);
        map.forEach((k, v) -> {
            System.out.println("key:" + k + " value:" + v + " value type:" + v.getClass().getSimpleName());
        });
    }
}
