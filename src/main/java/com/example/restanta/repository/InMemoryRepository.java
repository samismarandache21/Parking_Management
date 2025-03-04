package com.example.restanta.repository;

import com.example.restanta.model.Entity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class InMemoryRepository<ID extends Comparable<ID>, E extends Entity<ID>> implements Repository<ID, E> {
    protected Map<ID, E> entities;
    protected ID lastId;

    public InMemoryRepository() {
        this.entities = new HashMap<>();
        lastId = null;
    }

    @Override
    public ID getLastId() {
        return lastId;
    }

    @Override
    public Optional<E> findOne(ID id) {
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public List<E> findAll() {
        return List.copyOf(entities.values());
    }

    @Override
    public Optional<E> save(E entity) {
        if (entities.containsKey(entity.getId())) {
            return Optional.empty();
        }

        entities.put(entity.getId(), entity);

        if (lastId == null)
            lastId = entity.getId();
        else if (lastId.compareTo(entity.getId()) < 0)
            lastId = entity.getId();

        return Optional.of(entity);
    }
}
