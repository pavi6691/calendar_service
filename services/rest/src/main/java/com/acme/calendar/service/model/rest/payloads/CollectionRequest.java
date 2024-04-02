package com.acme.calendar.service.model.rest.payloads;
import com.acme.calendar.service.exceptions.validations.CreateValidationGroup;
import com.acme.calendar.service.exceptions.validations.UpdateValidationGroup;
import com.acme.calendar.service.model.IEntry;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ExecutableType;
import jakarta.validation.executable.ValidateOnExecution;
import lombok.Getter;
import lombok.Setter;
import java.time.ZonedDateTime;
import java.util.UUID;
@Setter
@Getter
public class CollectionRequest {
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
