package com.example.demo.entity;

import com.example.demo.annotation.Column;
import com.example.demo.annotation.Id;
import com.example.demo.annotation.Join;
import com.example.demo.annotation.Table;

@Table(name = "Cat")
public class Cat {
    @Id
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "typeId")
    @Join(foreignKey = "id", clazz = AnimalType.class)
    private AnimalType type;

    public AnimalType getType() {
        return type;
    }

    public void setType(AnimalType type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
