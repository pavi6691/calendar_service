package com.acme.calendar.service.controller;

import com.acme.calendar.core.CalendarConstants;
import com.acme.calendar.service.model.event.Event;
import com.acme.calendar.service.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(path = CalendarConstants.API_ENDPOINT_PREFIX + CalendarConstants.API_ENDPOINT_EVENTS)
public class EventController {

    EventService service;

    @Autowired
    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping(path = CalendarConstants.API_ENDPOINT_EVENTS_CREATE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Event> create(@RequestBody Event event) {
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
    public ResponseEntity<Event> update(@RequestBody Event event) {
        return ResponseEntity.ok(service.update(event));
    }

    @DeleteMapping(path = CalendarConstants.API_ENDPOINT_EVENTS_DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> delete(@RequestBody List<UUID> eventsUuids) {
        service.delete(eventsUuids);
        return ResponseEntity.ok("Done!");
    }

}
