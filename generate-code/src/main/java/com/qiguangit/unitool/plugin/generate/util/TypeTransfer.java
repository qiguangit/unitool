package com.qiguangit.unitool.plugin.generate.util;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeTransfer implements TemplateMethodModelEx {

    private Map<String, String> typeMap = new HashMap<>();

    public TypeTransfer() {
        typeMap.put("varchar", "String");
        typeMap.put("text", "String");
        typeMap.put("int", "Integer");
        typeMap.put("float", "Float");
        typeMap.put("", "");
    }

    private String transfer(String type) {
        return typeMap.get(type);
    }

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        SimpleScalar type = (SimpleScalar)arguments.get(0);
        return transfer(type == null ? "" : type.toString());
    }
}
