package com.acme.calendar.service.model.rest.payloads;
import com.acme.calendar.service.exceptions.validations.CreateValidationGroup;
import com.acme.calendar.service.exceptions.validations.UpdateValidationGroup;
import com.acme.calendar.service.model.IEntry;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
@Setter
@Getter
public class CollectionRequest implements IEntry<CollectionRequest> {
        @NotNull(groups = UpdateValidationGroup.class)
        String type;
        @NotNull(groups = UpdateValidationGroup.class)
        UUID uuid;
        @NotNull(groups = {CreateValidationGroup.class,UpdateValidationGroup.class})
        String title;
        String description;
        @NotNull(groups = UpdateValidationGroup.class)
        ZonedDateTime createdInitially;
        @NotNull(groups = UpdateValidationGroup.class)
        ZonedDateTime lastUpdatedTime;
        IEntry[] items;
 }
