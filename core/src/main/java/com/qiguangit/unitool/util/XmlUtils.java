package com.qiguangit.unitool.util;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class XmlUtils {

    private static final String ELEMENT_STRING = "string";
    private static final String ELEMENT_INT = "int";
    private static final String ELEMENT_FLOAT = "float";
    private static final String ELEMENT_BOOLEAN = "boolean";
    private static final String NAME = "name";
    private static final String VALUE = "value";
    private static final String ELEMENT_MAP = "map";
    private static final String ELEMENT_LONG = "long";

    private XmlUtils() {

    }

    public static Map<String, Object> readMapXml(File xmlFile) {
        HashMap<String, Object> map = new HashMap<>();
        if (xmlFile.exists()) {
            try {
                Document root = new SAXReader().read(xmlFile);
                Iterator<Element> mapIterator = root.getRootElement().elementIterator();
                while (mapIterator.hasNext()) {
                    Element element = mapIterator.next();
                    String type = element.getName();
                    String name = element.attributeValue(NAME);
                    switch (type) {
                        case ELEMENT_STRING:
                            map.put(name, element.getText());
                            break;
                        case ELEMENT_INT:
                            map.put(name, Integer.valueOf(element.attributeValue(VALUE)));
                            break;
                        case ELEMENT_LONG:
                            map.put(name, Long.valueOf(element.attributeValue(VALUE)));
                            break;
                        case ELEMENT_FLOAT:
                            map.put(name, Float.valueOf(element.attributeValue(VALUE)));
                            break;
                        case ELEMENT_BOOLEAN:
                            map.put(name, Boolean.valueOf(element.attributeValue(VALUE)));
                            break;
                        default:
                            break;
                    }
                }
            } catch (DocumentException e) {
                log.error("", e);
            }
        }

        return map;
    }

    public static void writeMapXml(Map<String, ?> mapToWriteToDisk, File xmlFile) {
        Document root = DocumentHelper.createDocument();
        Element map = root.addElement(ELEMENT_MAP);
        mapToWriteToDisk.forEach((k, v) -> {
            if (v instanceof String) {
                map.addElement(ELEMENT_STRING)
                   .addAttribute(NAME, k)
                   .setText((String) v);
            } else if (v instanceof Integer) {
                map.addElement(ELEMENT_INT)
                   .addAttribute(NAME, k)
                   .addAttribute(VALUE, String.valueOf(v));
            } else if (v instanceof Long) {
                map.addElement(ELEMENT_LONG)
                   .addAttribute(NAME, k)
                   .addAttribute(VALUE, String.valueOf(v));
            } else if (v instanceof Float || v instanceof Double) {
                map.addElement(ELEMENT_FLOAT)
                   .addAttribute(NAME, k)
                   .addAttribute(VALUE, String.valueOf(v));
            } else if (v instanceof Boolean) {
                map.addElement(ELEMENT_BOOLEAN)
                   .addAttribute(NAME, k)
                   .addAttribute(VALUE, String.valueOf(v));
            }
        });
        XMLWriter writer = null;
        try{
            OutputFormat format = new OutputFormat();
            format.setEncoding(StandardCharsets.UTF_8.name());
            format.setIndent(true);
            format.setTrimText(true);
            format.setNewlines(true);
            format.setPadText(true);

            writer = new XMLWriter(new FileWriter(xmlFile), format);
            writer.write(root);
        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    log.error("" , e);
                }
            }
        }
    }
}
