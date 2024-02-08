package com.acme.calendar.service.model;

import com.acme.calendar.service.model.calendar.Calendar;
import com.acme.calendar.service.model.collections.Collection;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Collection.class, name = "collection"),
        @JsonSubTypes.Type(value = Calendar.class, name = "calendar")
})
public interface IEntry<E> {
    UUID getUuid();
    String getTitle();
    String getDescription();
    
    default List<E> getItems() {return null;}
    default String getRrule() {return null;}
}
