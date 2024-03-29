package com.weareadaptive.auction.model;

import com.weareadaptive.auction.exception.BusinessException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class State<T extends Entity> {
    public static final String ITEM_ALREADY_EXISTS = "Item already exists";
    private final Map<Integer, T> entities;
    private int currentId = 1;

    public State() {
        entities = new HashMap<>();
    }

    public int nextId() {
        return currentId++;
    }

    protected void onAdd(final T model) {

    }

    public void add(final T model) {
        if (entities.containsKey(model.getId())) {
            throw new BusinessException(ITEM_ALREADY_EXISTS);
        }
        onAdd(model);
        entities.put(model.getId(), model);
    }

    protected Stream<T> stream() {
        return entities.values().stream();
    }

    protected T getEntity(final int id) {
        return entities.get(id);
    }
}
