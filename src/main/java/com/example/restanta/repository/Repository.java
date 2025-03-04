package com.example.restanta.repository;

import com.example.restanta.model.Entity;
import java.util.List;
import java.util.Optional;

public interface Repository<ID extends Comparable<ID>, E extends Entity<ID>> {
    ID getLastId();
    Optional<E> findOne(ID id);
    List<E> findAll();
    Optional<E> save(E entity);
}