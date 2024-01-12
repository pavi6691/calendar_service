package com.acme.calendar.model.group;

import java.util.UUID;


public record CUserGroup(

    UUID uuid,
    String name,
    String description

){}
