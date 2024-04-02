package com.acme.calendar.service.model.rest.payloads;
import com.acme.calendar.service.exceptions.validations.CreateValidationGroup;
import com.acme.calendar.service.exceptions.validations.UpdateValidationGroup;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;
import java.time.ZonedDateTime;

public record CalendarRequest (
        @NotNull(groups = UpdateValidationGroup.class)
        UUID uuid,
        @NotNull(groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
        String title,
        String description,
        @NotNull(groups = UpdateValidationGroup.class)
        ZonedDateTime createdInitially,
        @NotNull(groups = UpdateValidationGroup.class)
        ZonedDateTime lastUpdatedTime
) {}

