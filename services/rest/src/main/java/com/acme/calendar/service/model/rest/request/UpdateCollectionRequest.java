package com.acme.calendar.service.model.rest.request;

import com.acme.calendar.service.model.IEntry;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
public record UpdateCollectionRequest(
        @JsonProperty("items")
        List<IEntry> items
) {}
