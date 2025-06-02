package com.example.itemservice.exceptions.notfound;

public class CityNotFoundException extends NotFoundException {
    public CityNotFoundException(String name) {
        super("City with name " + name + " not found");
    }
}
