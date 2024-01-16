package com.acme.calendar.model.event;


public enum CRecurringQuarter {

    Q1 { @Override public boolean isQ1() { return true; }},
    Q2 { @Override public boolean isQ2() { return true; }},
    Q3 { @Override public boolean isQ3() { return true; }},
    Q4 { @Override public boolean isQ4() { return true; }};


    public boolean isQ1() { return false; }
    public boolean isQ2() { return false; }
    public boolean isQ3() { return false; }
    public boolean isQ4() { return false; }

}
