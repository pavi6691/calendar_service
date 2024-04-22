package com.acme.calendar.service.model.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class UserCollectionCalendarMapping {

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("Collection")
    private List<String> Collection;

    @JsonProperty("Calendar")
    private List<String> Calendar;

    // Constructor, getters, and setters
    public UserCollectionCalendarMapping() {
    }

    public UserCollectionCalendarMapping(String userName, List<String> collection,
        List<String> calendar) {
        this.userName = userName;
        Collection = collection;
        Calendar = calendar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getCollection() {
        return Collection;
    }

    public void setCollection(List<String> collection) {
        Collection = collection;
    }

    public List<String> getCalendar() {
        return Calendar;
    }

    public void setCalendar(List<String> calendar) {
        Calendar = calendar;
    }

    @Override
    public String toString() {
        return "UserData{" +
            "userName='" + userName + '\'' +
            ", Collection=" + Collection +
            ", Calendar=" + Calendar +
            '}';
    }
}