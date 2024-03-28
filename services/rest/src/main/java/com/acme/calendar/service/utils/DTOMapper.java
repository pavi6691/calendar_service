package com.acme.calendar.service.utils;


import com.acme.calendar.service.model.calendar.Calendar;
import com.acme.calendar.service.model.collections.Collection;
import com.acme.calendar.service.model.event.Event;
import com.acme.calendar.service.model.rest.payloads.CalendarRequest;
import com.acme.calendar.service.model.rest.payloads.CollectionCreateRequest;
import com.acme.calendar.service.model.rest.payloads.CollectionUpdateRequest;
import com.acme.calendar.service.model.rest.payloads.EventRequest;
import com.acme.calendar.service.model.rest.responses.CalendarResponse;
import com.acme.calendar.service.model.rest.responses.CollectionResponse;
import com.acme.calendar.service.model.rest.responses.EventResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DTOMapper {
    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);
    
    void copy(Collection from, @MappingTarget Collection entity);
    
    void copy(Calendar from, @MappingTarget Calendar entity);
    Event toEntity(EventRequest eventRequest);
    Calendar toEntity(CalendarRequest calendarRequest);
    Collection toEntity(CollectionCreateRequest collectionCreateRequest);
    Collection toEntity(CollectionUpdateRequest collectionUpdateRequest);
    CalendarResponse toCalendarResponseWithEvents(Calendar calendar);
    EventResponse toEventResponse(Event event);
    List<EventResponse> toEventResponse(List<Event> events);
    @Mapping(target = "events", ignore = true)
    CalendarResponse toCalendarResponseWithoutEvents(Calendar calendar);
    CollectionResponse toCollectionResponseWithItems(Collection collection);
    @Mapping(target = "items", ignore = true)
    CollectionResponse toCollectionResponseWithoutItems(Collection collection);
}
