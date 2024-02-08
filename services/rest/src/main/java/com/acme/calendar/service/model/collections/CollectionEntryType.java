package com.acme.calendar.service.model.collections;


public enum CollectionEntryType {

    COLLECTION      { @Override public boolean isCollection() { return true; }},
    CALENDAR { @Override public boolean isCalendar() { return true; }};


    public boolean isCollection()      { return false; }
    public boolean isCalendar() { return false; }

}
