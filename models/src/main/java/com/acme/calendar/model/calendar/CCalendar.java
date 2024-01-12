package com.acme.calendar.model.calendar;

import java.util.UUID;


public record CCalendar (

    UUID uuid,
    String title,
    String description

) {}
