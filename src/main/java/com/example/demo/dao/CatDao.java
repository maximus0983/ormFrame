package com.example.demo.dao;

import com.example.demo.entity.Cat;

public interface CatDao {
    int create(Cat cat);

    int update(Cat cat);

    void delete(Cat cat);

    Cat geById(int id);
}
