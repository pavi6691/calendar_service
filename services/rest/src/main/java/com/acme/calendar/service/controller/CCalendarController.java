package com.acme.calendar.service.controller;

import com.acme.calendar.core.CalendarConstants;
import com.acme.calendar.model.calendar.CCalendar;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(path = CalendarConstants.API_ENDPOINT_PREFIX + CalendarConstants.API_ENDPOINT_CALENDARS)
public class CCalendarController {


    @PostMapping(path = CalendarConstants.API_ENDPOINT_CALENDARS_CREATE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CCalendar> create(@RequestBody CCalendar cCalendar) {
        return ResponseEntity.ok(null);
    }

    @GetMapping(path = CalendarConstants.API_ENDPOINT_CALENDARS_GET_ALL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CCalendar>> getAll(Pageable pageable, Sort sort) {
        return ResponseEntity.ok(null);
    }

    @GetMapping(path = CalendarConstants.API_ENDPOINT_CALENDARS_GET_BY_UUID, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CCalendar> getByUuid(UUID uuid) {
        return ResponseEntity.ok(null);
    }

    @PutMapping(path = CalendarConstants.API_ENDPOINT_CALENDARS_UPDATE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CCalendar>> update(@RequestBody CCalendar cCalendar) {
        return ResponseEntity.ok(null);
    }

    @DeleteMapping(path = CalendarConstants.API_ENDPOINT_CALENDARS_DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CCalendar>> delete(@RequestBody List<UUID> cCalendars) {
        return ResponseEntity.ok(null);
    }

}
