package com.acme.calendar.service.model.rest.payloads;
import com.acme.calendar.model.validation.UpdateValidationGroup;
import com.acme.calendar.service.model.IEntry;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
public record CollectionCreateRequest(
        UUID uuid,
        @NotNull(groups = UpdateValidationGroup.class)
        String title,
        String description,
        ZonedDateTime createdInitially,
        ZonedDateTime lastUpdatedTime,
        IEntry[] items
){}
