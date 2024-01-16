package com.acme.calendar.service.model.event;

import java.util.UUID;


public record CEvent(

    UUID uuid,
    String title,
    String description
    // TODO add more fields here

) {}
