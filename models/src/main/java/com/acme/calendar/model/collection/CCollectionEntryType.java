package com.acme.calendar.model.collection;


public enum CCollectionEntryType {

    COLLECTION { @Override public boolean isCollection() { return true; }},
    CALENDAR { @Override public boolean isCalendar() { return true; }};


    public boolean isCollection() { return false; }
    public boolean isCalendar() { return false; }

}
