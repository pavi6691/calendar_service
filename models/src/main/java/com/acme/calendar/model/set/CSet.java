package com.acme.calendar.model.set;

import lombok.Builder;

import java.util.UUID;


@Builder
public record CSet(

    UUID uuid,
    String title,
    String description

) {}
