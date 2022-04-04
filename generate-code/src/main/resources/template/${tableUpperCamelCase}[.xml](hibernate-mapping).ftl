<#assign json="${data}"?eval />
<?xml version="1.0" encoding="UTF-8"?>
<!--映射文件的dtd约束信息-->
<!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
        "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">
<hibernate-mapping>
    <!-- name代表的是完整类名（包含的类名），table代表的是表名 -->
    <class name="${json.className}" table="${json.tableName}">
        <!-- name代表的是className类中的唯一标识属性，column代表的是tableName表中的主键id -->
        <id name="id" column="id">
            <!-- 主键生成策略 -->
            <generator class="native"/>
        </id>
        <!-- name表示className的普通属性 column表示tableName表的普通字段 type 表示字段类型-->
        <#list json.fields as field>
        <property name="${field.attrName}" type="${field.type}" not-null="${field.notNull}">
            <column name="${field.attrName}" length="${field.length}" default="${field.defaultValue}">
                <comment>${field.comment!}</comment>
            </column>
        </property>
        </#list>
    </class>
</hibernate-mapping>