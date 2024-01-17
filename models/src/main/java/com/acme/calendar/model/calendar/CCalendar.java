package com.acme.calendar.model.calendar;

import lombok.Builder;

import java.util.UUID;


@Builder
public record CCalendar (

    UUID uuid,
    String title,
    String description

) {}
