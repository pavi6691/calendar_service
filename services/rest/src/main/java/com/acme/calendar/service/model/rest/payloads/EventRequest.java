package com.acme.calendar.service.model.rest.payloads;
import com.acme.calendar.service.exceptions.validations.CreateValidationGroup;
import com.acme.calendar.service.exceptions.validations.UpdateValidationGroup;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;

public record EventRequest (
        
        @NotNull(groups = UpdateValidationGroup.class)
        UUID uuid,
        @NotNull(groups = {UpdateValidationGroup.class, CreateValidationGroup.class})
        String title,
        String description,
        @NotNull(groups = {UpdateValidationGroup.class, CreateValidationGroup.class})
        ZonedDateTime startTime,
        @NotNull(groups = {UpdateValidationGroup.class, CreateValidationGroup.class})
        ZonedDateTime endTime,
        @NotNull(groups = UpdateValidationGroup.class)
        ZonedDateTime createdInitially,
        @NotNull(groups = UpdateValidationGroup.class)
        ZonedDateTime lastUpdatedTime,
        String rrule
) {}
