package com.acme.calendar.service.model.rest.responses;
import com.acme.calendar.service.model.event.Event;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.UUID;
import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record CalendarResponse(
        UUID uuid,
        String title,
        String description,
        ZonedDateTime createdInitially,
        ZonedDateTime lastUpdatedTime,
        List<Event> events
) {}

