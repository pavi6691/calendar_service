package com.acme.calendar.model.set;

import com.acme.calendar.model.calendar.CCalendar;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;


@Builder
public record CSetEntry(

    CSet set,
    CCalendar calendar

) {

    @JsonIgnore
    public boolean isSet() {
        return set() != null;
    }

    @JsonIgnore
    public boolean isCalendar() {
        return calendar() != null;
    }

}
