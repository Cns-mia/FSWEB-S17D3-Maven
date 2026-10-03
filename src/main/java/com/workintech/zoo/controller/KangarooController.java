package com.workintech.zoo.controller;

import com.workintech.zoo.entity.Kangaroo;
import com.workintech.zoo.exceptions.ZooException;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/kangaroos")
public class KangarooController {

    private Map<Integer, Kangaroo> kangaroos;

    @PostConstruct
    public void init() {
        kangaroos = new HashMap<>();
    }

    @GetMapping
    public List<Kangaroo> findAll() {
        return new ArrayList<>(kangaroos.values());
    }

    @GetMapping("/{id}")
    public Kangaroo findById(@PathVariable Integer id) {
        validateId(id);
        Kangaroo kangaroo = kangaroos.get(id);
        if (kangaroo == null) {
            throw new ZooException("Kangaroo with given id is not exist: " + id, HttpStatus.NOT_FOUND);
        }
        return kangaroo;
    }

    @PostMapping
    public Kangaroo save(@RequestBody Kangaroo kangaroo) {
        validate(kangaroo);
        kangaroos.put(kangaroo.getId(), kangaroo);
        return kangaroo;
    }

    @PutMapping("/{id}")
    public Kangaroo update(@PathVariable Integer id, @RequestBody Kangaroo kangaroo) {
        validateId(id);
        kangaroo.setId(id);
        validate(kangaroo);
        kangaroos.put(id, kangaroo);
        return kangaroo;
    }

    @DeleteMapping("/{id}")
    public Kangaroo delete(@PathVariable Integer id) {
        validateId(id);
        Kangaroo removed = kangaroos.remove(id);
        if (removed == null) {
            throw new ZooException("Kangaroo with given id is not exist: " + id, HttpStatus.NOT_FOUND);
        }
        return removed;
    }

    private void validateId(Integer id) {
        if (id == null || id <= 0) {
            throw new ZooException("Id is not valid: " + id, HttpStatus.BAD_REQUEST);
        }
    }

    private void validate(Kangaroo kangaroo) {
        if (kangaroo == null || kangaroo.getId() <= 0
                || kangaroo.getName() == null || kangaroo.getName().isBlank()
                || kangaroo.getHeight() <= 0 || kangaroo.getWeight() <= 0
                || kangaroo.getGender() == null || kangaroo.getIsAggressive() == null) {
            throw new ZooException("Kangaroo credentials are not valid", HttpStatus.BAD_REQUEST);
        }
    }
}
