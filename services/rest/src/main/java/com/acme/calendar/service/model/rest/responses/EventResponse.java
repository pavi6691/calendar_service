package com.acme.calendar.service.model.rest.responses;
import com.acme.calendar.service.model.calendar.Calendar;
import com.acme.calendar.service.serialzation.ZonedDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.ZonedDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record EventResponse(
        UUID uuid,
        String title,
        String description,
        @JsonSerialize(using = ZonedDateTimeSerializer.class)
        ZonedDateTime startTime,
        @JsonSerialize(using = ZonedDateTimeSerializer.class)
        ZonedDateTime endTime,
        @JsonSerialize(using = ZonedDateTimeSerializer.class)
        ZonedDateTime createdInitially,
        @JsonSerialize(using = ZonedDateTimeSerializer.class)
        ZonedDateTime lastUpdatedTime,
        String rrule,
        @JsonIgnore
        Calendar calendar
) {}

