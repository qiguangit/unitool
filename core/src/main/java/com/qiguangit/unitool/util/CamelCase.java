package com.qiguangit.unitool.util;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CamelCase implements TemplateMethodModelEx {


    /**
     * 下划线转驼峰
     * @param str
     * @param mode lowerCamel upperCamel
     * */
    public String convertStr(String str, String mode) {
        Pattern linePattern = Pattern.compile("lowerCamel".equals(mode)? "_([a-z0-9]+)" : "([a-z0-9]+)_?");
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String subStr = matcher.group(1);
            String concat = subStr.substring(0, 1).toUpperCase().concat(subStr.substring(1));
            matcher.appendReplacement(sb, concat);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        SimpleScalar str = (SimpleScalar)arguments.get(0);
        SimpleScalar mode = (SimpleScalar)arguments.get(1);
        return convertStr(str == null ? "" : str.toString(), mode == null ? "" : mode.toString());
    }
}
