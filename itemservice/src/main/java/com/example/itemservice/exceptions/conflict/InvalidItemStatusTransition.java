package com.example.itemservice.exceptions.conflict;

import com.example.itemservice.domain.ItemStatus;

public class InvalidItemStatusTransition extends ConflictException {
    public InvalidItemStatusTransition(ItemStatus from, ItemStatus to) {
        super("Invalid item status transition from " + from + " to " + to);
    }
}
