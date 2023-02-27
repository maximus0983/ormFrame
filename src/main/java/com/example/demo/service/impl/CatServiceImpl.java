package com.example.demo.service.impl;

import com.example.demo.dao.CatDao;
import com.example.demo.entity.Cat;
import com.example.demo.service.CatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CatServiceImpl implements CatService {
    @Autowired
    private CatDao catDao;

    public int create(Cat cat) {
        return catDao.create(cat);
    }

    @Override
    public int update(Cat cat) {
        return catDao.update(cat);
    }

    @Override
    public void delete(Cat cat) {
        catDao.delete(cat);
    }

    @Override
    public Cat getById(int id) {
        return catDao.geById(id);
    }

}
