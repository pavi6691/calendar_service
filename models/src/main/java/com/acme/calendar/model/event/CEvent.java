package com.acme.calendar.model.event;

import com.acme.calendar.model.common.CTimePeriod;
import lombok.Builder;

import java.util.UUID;


@Builder
public record CEvent (

    UUID uuid,
    String title,
    String description,
    CTimePeriod period,
    CRecurring recurring

) {}
