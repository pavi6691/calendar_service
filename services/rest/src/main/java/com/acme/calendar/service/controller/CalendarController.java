package com.acme.calendar.service.controller;

import com.acme.calendar.core.CalendarConstants;
import com.acme.calendar.service.model.calendar.Calendar;
import com.acme.calendar.service.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(path = CalendarConstants.API_ENDPOINT_PREFIX + CalendarConstants.API_ENDPOINT_CALENDARS)
public class CalendarController {

    CalendarService service;
    
    @Autowired
    public CalendarController(CalendarService service) {
        this.service = service;
    }

    @PostMapping(path = CalendarConstants.API_ENDPOINT_CALENDARS_CREATE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Calendar> create(@RequestBody Calendar calendar) {
        return ResponseEntity.ok(service.create(calendar));
    }

    @GetMapping(path = CalendarConstants.API_ENDPOINT_CALENDARS_GET_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Calendar>> getAll(Pageable pageable, Sort sort) {
        return ResponseEntity.ok(service.getAll(pageable,sort));
    }

    @GetMapping(path = CalendarConstants.API_ENDPOINT_CALENDARS_GET_BY_UUID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Calendar> getByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(service.getByUuid(uuid));
    }

    @PutMapping(path = CalendarConstants.API_ENDPOINT_CALENDARS_UPDATE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Calendar> update(@RequestBody Calendar calendar) throws Exception {
        return ResponseEntity.ok(service.update(calendar));
    }

    @DeleteMapping(path = CalendarConstants.API_ENDPOINT_CALENDARS_DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> delete(@RequestBody List<UUID> calendarsUuids) {
        service.delete(calendarsUuids);
        return ResponseEntity.ok("Done!");
    }

}
