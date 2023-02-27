package com.example.demo.controller;

import com.example.demo.entity.Cat;
import com.example.demo.service.CatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cats")
public class Controller {
    @Autowired
    private CatService catService;

    @PostMapping()
    public int create(@RequestBody Cat cat) {
        return catService.create(cat);
    }

    @PutMapping()
    public int update(@RequestBody Cat cat) {
        return catService.update(cat);
    }

    @DeleteMapping()
    public void delete(@RequestBody Cat cat) {
        catService.delete(cat);
    }

    @GetMapping("/{id}")
    public Cat getById(@PathVariable Integer id) {
        return catService.getById(id);
    }


}
