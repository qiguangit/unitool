package com.qiguangit.unitool.plugin.generate.util;

import com.qiguangit.unitool.util.SharedPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableUtils {

    private static final Map<String, String> VARIABLES = new HashMap<>();
    private static final SharedPreferences sp = SharedPreferences.getSharedPreferences("generate");
    static {
        VARIABLES.put("tableUpperCamelCase", VARIABLES.get("tableUpperCamelCase"));
    }

    private VariableUtils() {

    }

    public static Map<String, Object> getVariables() {
        return new HashMap<>(VARIABLES);
    }

    public static void put(String key, String value) {
        VARIABLES.put(key, value);
    }

    public static String get(String key) {
        return VARIABLES.get(key);
    }

    public static void remove(String key) {
        VARIABLES.remove(key);
    }

    public static void clear() {
        VARIABLES.clear();
    }

    public static String parseFileName(String templateFileName) {
        String replace = parse_(templateFileName, "\\(", "\\)").replace(".ftl", "");
        replace = parse_(replace, "#\\{", "}");
        String parseVariable = parse(replace, "\\$\\{", "}");
        return parse(parseVariable, "\\[", "\\]");
    }

    public static String getPath(String templateFileName) {
        Pattern linePattern = Pattern.compile("#\\{\\S+}");
        Matcher matcher = linePattern.matcher(templateFileName);
        if (matcher.find()) {
            String subStr = matcher.group();
            return subStr.replaceAll("#\\{", "").replaceAll("}", "");
        }
        return "";
    }

    public static void main(String[] args) {
        System.out.println(VariableUtils.parseFileName("${tableUpperCamelCase}[.java]#{com.qiguangit.entity}(entity).ftl"));
    }

    public static String getComment(String templateFileName) {
        Pattern linePattern = Pattern.compile("\\(\\S+\\)");
        Matcher matcher = linePattern.matcher(templateFileName);
        if (matcher.find()) {
            String subStr = matcher.group();
            return subStr.replaceAll("\\(", "").replaceAll("\\)", "");
        }
        return templateFileName;
    }

    private static String parse(String templateFileName, String prefix, String suffix) {
        Pattern linePattern = Pattern.compile(prefix + "\\S+" + suffix);
        Matcher matcher = linePattern.matcher(templateFileName);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String subStr = matcher.group();
            String concat = subStr.replaceAll(prefix, "").replaceAll(suffix, "");
            matcher.appendReplacement(sb, sp.getString(concat, concat));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String parse_(String templateFileName, String prefix, String suffix) {
        Pattern linePattern = Pattern.compile(prefix + "\\S+" + suffix);
        Matcher matcher = linePattern.matcher(templateFileName);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
