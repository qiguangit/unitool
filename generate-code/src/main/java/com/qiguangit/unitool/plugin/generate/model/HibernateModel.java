package com.qiguangit.unitool.plugin.generate.model;

import java.io.Serializable;
import java.util.List;

public class HibernateModel implements Serializable{
    private String className;
    private String tableName;
    private String packageName;
    private List<Field> fields;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "HibernateModel{" +
                "className='" + className + '\'' +
                ", tableName='" + tableName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", fields=" + fields +
                '}';
    }

    public static class Field implements Serializable {
        private String attrName;
        private String type;
        private String notNull;
        private String fieldName;
        private String comment;
        private String length;
        private Object defaultValue;

        public Field() {
        }

        public Field(String attrName, String type, String notNull) {
            this.attrName = attrName;
            this.type = type;
            this.notNull = notNull;
        }

        public String getAttrName() {
            return attrName;
        }

        public void setAttrName(String attrName) {
            this.attrName = attrName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNotNull() {
            return notNull;
        }

        public void setNotNull(String notNull) {
            this.notNull = notNull;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getLength() {
            return length;
        }

        public void setLength(String length) {
            this.length = length;
        }

        public Object getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(Object defaultValue) {
            this.defaultValue = defaultValue;
        }

        @Override
        public String toString() {
            return "Field{" +
                    "attrName='" + attrName + '\'' +
                    ", type='" + type + '\'' +
                    ", notNull=" + notNull +
                    ", fieIdName='" + fieldName + '\'' +
                    ", comment='" + comment + '\'' +
                    ", length=" + length +
                    ", defaultValue=" + defaultValue +
                    '}';
        }
    }
}
