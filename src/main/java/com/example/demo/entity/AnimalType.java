package com.example.demo.entity;

import com.example.demo.annotation.Id;
import com.example.demo.annotation.Table;

@Table(name = "Animal_Type")
public class AnimalType {
    @Id
    private int id;
    private String name;

    public AnimalType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public AnimalType() {

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
