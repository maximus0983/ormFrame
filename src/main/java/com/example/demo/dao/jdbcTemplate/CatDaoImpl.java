package com.example.demo.dao.jdbcTemplate;

import com.example.demo.dao.CatDao;
import com.example.demo.dao.SQlGenerator;
import com.example.demo.entity.AnimalType;
import com.example.demo.entity.Cat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CatDaoImpl implements CatDao {
    @Autowired
    private SQlGenerator sqlGenerator;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public int create(Cat cat) {
        String query = sqlGenerator.create(cat);
        return jdbcTemplate.update(query);
    }

    @Override
    public int update(Cat cat) {
        String query = sqlGenerator.update(cat);
        return jdbcTemplate.update(query);
    }

    @Override
    public void delete(Cat cat) {
        String query = sqlGenerator.delete(cat.getId(), Cat.class);
        jdbcTemplate.update(query);
    }

    @Override
    public Cat geById(int id) {
        String query = sqlGenerator.getById(id, Cat.class);
        return jdbcTemplate.queryForObject(query, (rs, rowNum) -> {
            Cat cat = new Cat();
            cat.setId(rs.getInt(1));
            cat.setName(rs.getString(2));
            AnimalType type = new AnimalType();
            type.setId(rs.getInt(3));
            type.setName(rs.getString(4));
            cat.setType(type);
            return cat;
        });
    }


}
