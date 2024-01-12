package com.acme.calendar.model.event;

import java.util.UUID;


// TODO Maybe all 4 CEvent* classes should be moved to the service if information is for internal use, and be replaced by a simpler CEvent here?
public record CEvent(

    UUID uuid,
    String title,
    String description
    // TODO add more fields here

) {}
