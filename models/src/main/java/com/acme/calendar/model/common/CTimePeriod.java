package com.acme.calendar.model.common;

import lombok.Builder;

import java.time.ZonedDateTime;


@Builder
public record CTimePeriod(

    ZonedDateTime start,
    ZonedDateTime end

) {}
