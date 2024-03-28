package com.acme.calendar.service.model.rest.responses;
import com.acme.calendar.service.serialzation.ZonedDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;
import java.util.UUID;
import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record CalendarResponse(
        UUID uuid,
        String title,
        String description,
        @JsonSerialize(using = ZonedDateTimeSerializer.class)
        ZonedDateTime createdInitially,
        @JsonSerialize(using = ZonedDateTimeSerializer.class)
        ZonedDateTime lastUpdatedTime,
        List<EventResponse> events
) {}

