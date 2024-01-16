package com.acme.calendar.model.event;

import java.util.List;


public record CRecurring(

    CRecurringFrequency frequency,
    Integer every,
    Integer repeat,
    List<Integer> weekDays,
    Integer weekNumber,
    List<Integer> months,
    List<Integer> monthDay,
    List<CRecurringQuarter> quarters

) {}
