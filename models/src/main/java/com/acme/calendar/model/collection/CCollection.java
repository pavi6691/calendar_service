package com.acme.calendar.model.collection;

import lombok.Builder;

import java.util.List;
import java.util.UUID;


@Builder
public record CCollection(

    UUID uuid,
    String title,
    String description,
    List<CCollectionEntry> entries

) {}
