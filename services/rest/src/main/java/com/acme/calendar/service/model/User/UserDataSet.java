package com.acme.calendar.service.model.User;

import java.util.List;

public class UserDataSet {
    private List<UserCollectionCalendarMapping> items;

    // Constructor, getters, and setters
    public UserDataSet() {}

    public UserDataSet(List<UserCollectionCalendarMapping> items) {
        this.items = items;
    }

    public List<UserCollectionCalendarMapping> getItems() {
        return items;
    }

    public void setItems(List<UserCollectionCalendarMapping> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "UserDataSet{" +
            "items=" + items +
            '}';
    }
}
