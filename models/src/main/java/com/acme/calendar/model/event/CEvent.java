package com.acme.calendar.model.event;

import com.acme.calendar.model.common.CTimePeriod;

import java.util.UUID;


public record CEvent (

    UUID uuid,
    String title,
    String description,
    CTimePeriod period,
    CRecurring recurring

) {}
