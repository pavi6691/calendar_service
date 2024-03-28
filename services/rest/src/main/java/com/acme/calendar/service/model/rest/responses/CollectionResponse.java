package com.acme.calendar.service.model.rest.responses;
import com.acme.calendar.service.model.IEntry;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record CollectionResponse (
        UUID uuid,
        String title,
        String description,
        ZonedDateTime createdInitially,
        ZonedDateTime lastUpdatedTime,
        IEntry[] items
)  {}

