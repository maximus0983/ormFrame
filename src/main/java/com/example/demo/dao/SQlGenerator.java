package com.example.demo.dao;

public interface SQlGenerator {
    String create(Object o);

    String update(Object o);

    String delete(int id, Class<?> clazz);

    String getById(int id, Class<?> clazz);
}


