package com.acme.calendar.model.set;

import com.acme.calendar.model.calendar.CCalendar;
import lombok.Builder;


@Builder
public record CSetEntry(

    CSetEntryType type,
    CSet set,
    CCalendar calendar

) {}
