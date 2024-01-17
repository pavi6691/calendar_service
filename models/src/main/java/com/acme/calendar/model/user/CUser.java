package com.acme.calendar.model.user;

import lombok.Builder;

import java.util.UUID;


@Builder
public record CUser(

    UUID uuid,
    String socialId,
    String username,
    String password

) {}
