package com.acme.calendar.model.set;

import java.util.UUID;


public record CSet(

    UUID uuid,
    String title,
    String description

) {}
