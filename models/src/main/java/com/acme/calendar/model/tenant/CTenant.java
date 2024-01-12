package com.acme.calendar.model.tenant;

import java.util.UUID;


public record CTenant(

    UUID uuid,
    String name,
    String description

) {}
