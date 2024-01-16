package com.acme.calendar.service.model.event;

import java.util.UUID;


// TODO Maybe this should be an enum instead?
public record CEventRecurringType(

    UUID uuid,
    String type

) {}
