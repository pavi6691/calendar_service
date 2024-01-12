package com.acme.calendar.model.user;

import java.util.UUID;


public record CUser(

    UUID uuid,
    String socialId,
    String username,
    String password

) {}
