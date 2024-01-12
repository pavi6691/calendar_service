package com.acme.calendar.service.controller;

import com.acme.calendar.core.CalendarConstants;
import com.acme.calendar.model.event.CEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(path = CalendarConstants.API_ENDPOINT_PREFIX + CalendarConstants.API_ENDPOINT_EVENTS)
public class CEventController {


    @PostMapping(path = CalendarConstants.API_ENDPOINT_EVENTS_CREATE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CEvent> create(@RequestBody CEvent cEvent) {
        return ResponseEntity.ok(null);
    }

    @GetMapping(path = CalendarConstants.API_ENDPOINT_EVENTS_GET_ALL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CEvent>> getAll(Pageable pageable, Sort sort) {
        return ResponseEntity.ok(null);
    }

    @GetMapping(path = CalendarConstants.API_ENDPOINT_EVENTS_GET_BY_UUID, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CEvent> getByUuid(UUID uuid) {
        return ResponseEntity.ok(null);
    }

    @PutMapping(path = CalendarConstants.API_ENDPOINT_EVENTS_UPDATE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CEvent>> update(@RequestBody CEvent cEvent) {
        return ResponseEntity.ok(null);
    }

    @DeleteMapping(path = CalendarConstants.API_ENDPOINT_EVENTS_DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CEvent>> delete(@RequestBody List<UUID> cEvents) {
        return ResponseEntity.ok(null);
    }

}
