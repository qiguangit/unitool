package com.qiguangit.unitool.plugin.generate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
public class TableModel implements Serializable{
    private String projectPath;
    private String className;
    private String tableName;
    private String packageName;
    private List<Field> fields;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Field implements Serializable {
        private String attrName;
        private String type;
        private String notNull;
        private String comment;
        private String length;
        private Object defaultValue;
    }
}
