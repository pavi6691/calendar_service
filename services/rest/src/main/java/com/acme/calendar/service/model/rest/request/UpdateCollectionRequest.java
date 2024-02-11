package com.acme.calendar.service.model.rest.request;

import com.acme.calendar.service.model.IEntry;
import com.acme.calendar.service.model.collections.Collection;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record UpdateCollectionRequest(
        UUID uuid,
        @JsonProperty("items")
        List<IEntry> items
) {}
