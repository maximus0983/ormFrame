package com.example.demo.dao.jdbcTemplate;

import com.example.demo.annotation.Column;
import com.example.demo.annotation.Id;
import com.example.demo.annotation.Join;
import com.example.demo.annotation.Table;
import com.example.demo.dao.SQlGenerator;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AnnotationGenerator implements SQlGenerator {
    private static final String ID = "id";

    @Override
    public String create(Object o) {
        Class<?> clazz = o.getClass();
        Table annotation = clazz.getAnnotation(Table.class);
        String tableName = annotation.name();
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + "(");
        Field[] declaredFields = clazz.getDeclaredFields();
        List<String> columnNames = new ArrayList<>(declaredFields.length);
        List<String> values = new ArrayList<>(declaredFields.length);
        extractFieldNamesAndValues(o, declaredFields, columnNames, values);
        sql.append(separateListValuesToString(columnNames))
                .append(") VALUES(")
                .append(separateListValuesToString(values))
                .append(")");

        return sql.toString();
    }

    @Override
    public String update(Object o) {
        try {
            Class<?> clazz = o.getClass();
            Table annotation = clazz.getAnnotation(Table.class);
            String tableName = annotation.name();
            StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
            Field[] declaredFields = clazz.getDeclaredFields();
            String id = null;
            id = getId(declaredFields, o);
            List<String> columnNames = new ArrayList<>(declaredFields.length);
            List<String> values = new ArrayList<>(declaredFields.length);
            extractFieldNamesAndValues(o, declaredFields, columnNames, values);
            List<String> colEqualVal = joinColumnValues(columnNames, values);
            sql.append(separateListValuesToString(colEqualVal))
                    .append(" WHERE id = ")
                    .append(id);
            return sql.toString();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String delete(int id, Class<?> clazz) {
        Table annotation = clazz.getAnnotation(Table.class);
        String tableName = annotation.name();
        return "DELETE FROM " + tableName + " WHERE id = " + id;
    }

    @Override
    public String getById(int id, Class<?> clazz) {
        Table annotation = clazz.getAnnotation(Table.class);
        String tableName = annotation.name();
        StringBuilder sql = new StringBuilder("SELECT " + tableName + ".*");
        List<String> selectValues = new ArrayList<>();
        List<String> joinTablesValues = new ArrayList<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            Join join = field.getAnnotation(Join.class);
            if (Objects.nonNull(join)) {
                field.setAccessible(true);
                recursionForJoin(field, selectValues, joinTablesValues, join, tableName);
            }
        }
        sql.append(", ")
                .append(separateListValuesToString(selectValues))
                .append(" FROM ")
                .append(tableName)
                .append(String.join(" ", joinTablesValues))
                .append("WHERE ")
                .append(tableName + ".id = " + id);
        return sql.toString();
    }

    private void recursionForJoin(Field parentField, List<String> selectValues, List<String> joinTablesValues,
                                  Join parentJoin, String parentTableName) {
        String foreignKey = parentJoin.foreignKey();
        Class<?> clazz = parentJoin.clazz();
        String tableName = clazz.getAnnotation(Table.class).name();
        String selectValue = tableName + ".*";
        selectValues.add(selectValue);
        String columnName = getColumnName(parentField);
        String joinTabValue = " join " + tableName + " on " + parentTableName + "." + columnName + " = " + tableName + "." + foreignKey + "\n";
        joinTablesValues.add(joinTabValue);
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            Join join = field.getAnnotation(Join.class);
            if (Objects.nonNull(join)) {
                field.setAccessible(true);
                recursionForJoin(field, selectValues, joinTablesValues, join, tableName);
            }
        }

    }

    private List<String> joinColumnValues(List<String> columnNames, List<String> values) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            result.add(columnNames.get(i) + "=" + values.get(i));
        }
        return result;
    }

    private String separateListValuesToString(List<String> columnNames) {
        return String.join(", ", columnNames);
    }

    private String getValueAsString(Field field, Object o) throws IllegalAccessException {
        field.setAccessible(true);
        Class<?> type = field.getType();
        String s = field.get(o).toString();
        if (type.equals(String.class)) {
            s = "\"" + s + "\"";
        }
        return s;
    }

    private void extractFieldNamesAndValues(Object o, Field[] declaredFields, List<String> columnNames, List<String> values) {
        try {
            for (Field field : declaredFields) {
                String columnName = getColumnName(field);
                if (!ID.equals(columnName)) {
                    columnNames.add(columnName);
                    Join join = field.getAnnotation(Join.class);
                    if (Objects.nonNull(join)) {
                        String valueFromJoin;
                        valueFromJoin = getValueFromJoin(field, join, o);
                        values.add(valueFromJoin);
                    } else {
                        values.add(getValueAsString(field, o));
                    }
                }
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

    }

    private String getId(Field[] fields, Object o) throws IllegalAccessException {
        for (Field field : fields) {
            Id annotation = field.getAnnotation(Id.class);
            if (annotation != null) {
                return getValueAsString(field, o);
            }
        }
        return null;
    }

    private String getValueFromJoin(Field field, Join join, Object obj) throws IllegalAccessException, NoSuchFieldException {
        Class<?> clazz = join.clazz();
        field.setAccessible(true);
        Object o = field.get(obj);
        Field foreignKey = clazz.getDeclaredField(join.foreignKey());
        return getValueAsString(foreignKey, o);
    }

    private String getColumnName(Field field) {
        String columnName;
        Column annotatedField = field.getAnnotation(Column.class);
        if (Objects.nonNull(annotatedField)) {
            columnName = annotatedField.name();
        } else columnName = field.getName();

        return columnName;
    }
}
