package com.acme.calendar.model.event;


public enum CRecurringFrequency {

    DAILY     { @Override public boolean isDaily()     { return true; }},
    WEEKLY    { @Override public boolean isWeekly()    { return true; }},
    MONTHLY   { @Override public boolean isMonthly()   { return true; }},
    QUARTERLY { @Override public boolean isQuarterly() { return true; }},          // TODO Check if this can be removed in favor of standard RFC 5546 spec somehow
    YEARLY    { @Override public boolean isYearly()    { return true; }};


    public boolean isDaily()     { return false; }
    public boolean isWeekly()    { return false; }
    public boolean isMonthly()   { return false; }
    public boolean isQuarterly() { return false; }
    public boolean isYearly()    { return false; }

}
