package com.example.demo.service;

import com.example.demo.entity.Cat;

public interface CatService {
    int create(Cat cat);

    int update(Cat cat);

    void delete(Cat cat);

    Cat getById(int id);
}
