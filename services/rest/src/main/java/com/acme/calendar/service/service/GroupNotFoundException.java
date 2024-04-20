package com.acme.calendar.service.service;

public class GroupNotFoundException extends Exception {
    public GroupNotFoundException(String message) {
        super(message);
    }
}