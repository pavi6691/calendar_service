package com.acme.calendar.model.common;

import java.time.ZonedDateTime;


public record CTimePeriod(

    ZonedDateTime start,
    ZonedDateTime end

) {}
