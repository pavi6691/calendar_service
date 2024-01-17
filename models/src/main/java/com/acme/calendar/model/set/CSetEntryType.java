package com.acme.calendar.model.set;


public enum CSetEntryType {

    SET      { @Override public boolean isSet()      { return true; }},
    CALENDAR { @Override public boolean isCalendar() { return true; }};


    public boolean isSet()      { return false; }
    public boolean isCalendar() { return false; }

}
