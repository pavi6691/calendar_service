package com.acme.calendar.service.model;
import com.acme.calendar.service.exceptions.validations.UpdateValidationGroup;
import com.acme.calendar.service.model.calendar.Calendar;
import com.acme.calendar.service.model.collections.Collection;
import com.acme.calendar.service.model.rest.payloads.CollectionRequest;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY, 
        defaultImpl = CollectionRequest.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Collection.class, name = "collection"),
        @JsonSubTypes.Type(value = Calendar.class, name = "calendar")
})
public interface IEntry<E> {
    @NotNull(groups = UpdateValidationGroup.class)
    String getType();
    UUID getUuid();
    String getTitle();
    String getDescription();
    default Object[] getCollection(int size){return null;}
}
