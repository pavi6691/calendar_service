package com.acme.calendar.service.controller;

import com.acme.calendar.core.CalendarConstants;
import com.acme.calendar.service.exceptions.validations.UpdateValidationGroup;
import com.acme.calendar.service.model.calendar.Calendar;
import com.acme.calendar.service.model.event.Event;
import com.acme.calendar.service.model.rest.payloads.EventRequest;
import com.acme.calendar.service.service.EventService;
import java.time.ZonedDateTime;

import com.acme.calendar.service.utils.DTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = CalendarConstants.API_ENDPOINT_PREFIX + CalendarConstants.API_ENDPOINT_EVENTS)
public class EventController {

    EventService service;

    @Autowired
    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping(path = CalendarConstants.API_ENDPOINT_EVENTS_CREATE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Event> create(@PathVariable UUID calendarUuid, @RequestBody EventRequest eventRequest) {
        Event event = DTOMapper.INSTANCE.toEntity(eventRequest);
        event.setCalendar(Calendar.builder().uuid(calendarUuid).build());
        return ResponseEntity.ok(service.create(event));
    }

    @GetMapping(path = CalendarConstants.API_ENDPOINT_EVENTS_GET_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Event>> getAll(Pageable pageable, Sort sort) {
        return ResponseEntity.ok(service.getAll(pageable,sort));
    }

    @GetMapping(path = CalendarConstants.API_ENDPOINT_EVENTS_GET_BY_UUID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Event> getByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(service.getByUuid(uuid));
    }

    @PutMapping(path = CalendarConstants.API_ENDPOINT_EVENTS_UPDATE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Event> update(@RequestBody @Validated(UpdateValidationGroup.class) EventRequest eventRequest) {
        Event event = DTOMapper.INSTANCE.toEntity(eventRequest);
        return ResponseEntity.ok(service.update(event));
    }

    @DeleteMapping(path = CalendarConstants.API_ENDPOINT_EVENTS_DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> delete(@RequestBody List<UUID> eventsUuids) {
        service.delete(eventsUuids);
        return ResponseEntity.ok("");
    }

    @GetMapping(path = CalendarConstants.API_ENDPOINT_EVENTS_BETWEEN_TIME, produces = MediaType.APPLICATION_JSON_VALUE )
    public List<Event> getEventsBetweenDates(@RequestParam UUID calendarUuid, @RequestParam  ZonedDateTime startDate, @RequestParam  ZonedDateTime endDate) {
        return service.getEventsBetweenDates(calendarUuid, startDate, endDate);
    }

}
