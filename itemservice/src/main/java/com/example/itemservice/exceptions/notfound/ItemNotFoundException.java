package com.example.itemservice.exceptions.notfound;

public class ItemNotFoundException extends NotFoundException {
    public ItemNotFoundException(Long id) {
        super("Item with id " + id + " not found");
    }
}
