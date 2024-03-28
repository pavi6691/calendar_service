package com.acme.calendar.service.model.rest.payloads;
import com.acme.calendar.model.validation.UpdateValidationGroup;
import com.acme.calendar.service.model.IEntry;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
@Setter
@Getter
public class CollectionUpdateRequest implements IEntry<CollectionUpdateRequest> {
        String type = "collection";
        @NotNull(groups = UpdateValidationGroup.class)
        UUID uuid;
        @NotNull(groups = UpdateValidationGroup.class)
        String title;
        String description;
        @NotNull(groups = UpdateValidationGroup.class)
        ZonedDateTime createdInitially;
        @NotNull(groups = UpdateValidationGroup.class)
        ZonedDateTime lastUpdatedTime;
        IEntry[] items;
 }
