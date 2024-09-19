package com.goody.utils.zisun;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * insert ignore into plugin
 *
 * @author Goody
 * @version 1.0, 2023/7/17
 * @since 1.0.0
 */
public class InsertIgnoreIntoPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        // add import
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Insert"));
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));
        interfaze.addImportedType(new FullyQualifiedJavaType("java.util.Collection"));
        // add method
        interfaze.addMethod(this.insertIgnoreIntoOne(interfaze, introspectedTable));
        interfaze.addMethod(this.insertIgnoreIntoBatch(interfaze, introspectedTable));

        return super.clientGenerated(interfaze, introspectedTable);
    }

    private Method insertIgnoreIntoOne(Interface interfaze, IntrospectedTable introspectedTable) {
        // Clazz
        final FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        // Clazz record
        final Parameter parameters = new Parameter(type, "record");
        // @Param("item") Clazz record
        parameters.addAnnotation("@Param(\"item\")");

        // void insertIgnoreCustom() {}
        final Method method = new Method("insertIgnoreCustom");
        // void insertIgnoreCustom();
        method.setAbstract(true);
        // void insertIgnoreCustom(@Param("item") Collection<Clazz> records);
        method.addParameter(parameters);

        final String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        final String columnNames = introspectedTable.getAllColumns()
            .stream()
            .map(column -> String.format("`%s`", column.getActualColumnName()))
            .collect(Collectors.joining(", "));
        final String columnValueNames = introspectedTable.getAllColumns()
            .stream()
            .map(column -> String.format("#{item.%s}", column.getJavaProperty()))
            .collect(Collectors.joining(", "));
        String insertIgnore = String.format("@Insert({" +
            "\"<script>\" +\n" +
            "            \" INSERT IGNORE INTO %s\" +\n" +
            "              \" (%s)\" +\n" +
            "            \" VALUES\" +\n" +
            "              \"(%s)\" +\n" +
            "        \"</script>\"" +
            "})", tableName, columnNames, columnValueNames);
        method.addAnnotation(insertIgnore);
        return method;
    }

    private Method insertIgnoreIntoBatch(Interface interfaze, IntrospectedTable introspectedTable) {
        // Collection<Clazz>
        final FullyQualifiedJavaType type = new FullyQualifiedJavaType(String.format("Collection<%s>", introspectedTable.getBaseRecordType()));
        // Collection<Clazz> records
        final Parameter parameters = new Parameter(type, "records");
        // @Param("items") Collection<Clazz> records
        parameters.addAnnotation("@Param(\"items\")");

        // void insertIgnoreBatchCustom() {}
        final Method method = new Method("insertIgnoreBatchCustom");
        // void insertIgnoreBatchCustom();
        method.setAbstract(true);
        // void insertIgnoreBatchCustom(@Param("items") Collection<Clazz> records);
        method.addParameter(parameters);

        final String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        final String columnNames = introspectedTable.getAllColumns()
            .stream()
            .map(column -> String.format("`%s`", column.getActualColumnName()))
            .collect(Collectors.joining(", "));
        final String columnValueNames = introspectedTable.getAllColumns()
            .stream()
            .map(column -> String.format("#{item.%s}", column.getJavaProperty()))
            .collect(Collectors.joining(", "));
        String insertIgnore = String.format("@Insert({" +
            "\"<script>\" +\n" +
            "            \" INSERT IGNORE INTO %s\" +\n" +
            "            \" (%s)\" +\n" +
            "            \" VALUES\" +\n" +
            "            \"<foreach collection='items' item='item' separator=','>\" +\n" +
            "               \"(%s)\" +\n" +
            "            \"</foreach> \" +\n" +
            "        \"</script>\"" +
            "})", tableName, columnNames, columnValueNames);
        method.addAnnotation(insertIgnore);
        return method;
    }
}
