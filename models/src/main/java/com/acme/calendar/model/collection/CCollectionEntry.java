package com.acme.calendar.model.collection;

import com.acme.calendar.model.calendar.CCalendar;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;


@Builder
public record CCollectionEntry(

    CCollection collection,
    CCalendar calendar

) {

    @JsonIgnore
    public boolean isCollection() {
        return collection() != null;
    }

    @JsonIgnore
    public boolean isCalendar() {
        return calendar() != null;
    }

}
