package com.acme.calendar.model.group;

import lombok.Builder;

import java.util.UUID;


@Builder
public record CUserGroup(

    UUID uuid,
    String name,
    String description

){}
