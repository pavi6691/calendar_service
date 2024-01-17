package com.acme.calendar.model.tenant;

import lombok.Builder;

import java.util.UUID;


@Builder
public record CTenant(

    UUID uuid,
    String name,
    String description

) {}
