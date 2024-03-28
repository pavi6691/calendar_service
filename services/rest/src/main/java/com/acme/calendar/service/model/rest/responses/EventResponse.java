package com.acme.calendar.service.model.rest.responses;

import com.acme.calendar.service.model.calendar.Calendar;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record EventResponse(
        UUID uuid,
        String title,
        String description,
        ZonedDateTime startTime,
        ZonedDateTime endTime,
        ZonedDateTime createdInitially,
        ZonedDateTime lastUpdatedTime,
        String rrule,
        @JsonIgnore
        Calendar calendar
) {}

