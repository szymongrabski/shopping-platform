package com.example.itemservice.exceptions.conflict;

import com.example.itemservice.domain.ItemStatus;

public class ItemNotModifiable extends ConflictException {
    public ItemNotModifiable(Long id, ItemStatus status) {
        super("Item " + id + " not modifiable because status is " + status.name());
    }
}
