package com.acme.calendar.service.controller;

import com.acme.calendar.core.CalendarConstants;
import com.acme.calendar.service.model.calendar.Calendar;
import com.acme.calendar.service.model.calendar.CalendarMapping;
import com.acme.calendar.service.model.collections.Collection;
import com.acme.calendar.service.model.rest.payloads.CalendarRequest;
import com.acme.calendar.service.model.rest.responses.CalendarResponse;
import com.acme.calendar.service.service.CalendarService;
import com.acme.calendar.service.utils.DTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Set;
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
    public ResponseEntity<CalendarResponse> create(@PathVariable UUID collectionUuid, @RequestBody CalendarRequest calendarRequest) {
        Calendar calendar = DTOMapper.INSTANCE.toEntity(calendarRequest);
        calendar.setMappings((Set.of(CalendarMapping.builder().parent(Collection.builder().uuid(collectionUuid).build()).build())));
        return ResponseEntity.ok(service.create(calendar));
    }

    @GetMapping(path = CalendarConstants.API_ENDPOINT_CALENDARS_GET_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CalendarResponse>> getAll(@RequestParam(defaultValue = "false") boolean includeEvents, Pageable pageable, Sort sort) {
        return ResponseEntity.ok(service.getAll(pageable,sort,includeEvents));
    }

    @GetMapping(path = CalendarConstants.API_ENDPOINT_CALENDARS_GET_BY_UUID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CalendarResponse> getByUuid(@RequestParam UUID calendarUuid, @RequestParam(defaultValue = "false") boolean includeEvents) {
        return ResponseEntity.ok(service.getByUuid(calendarUuid,includeEvents));
    }

    @PutMapping(path = CalendarConstants.API_ENDPOINT_CALENDARS_UPDATE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CalendarResponse> update(@RequestBody CalendarRequest calendarRequest) throws Exception {
        Calendar calendar = DTOMapper.INSTANCE.toEntity(calendarRequest);
        return ResponseEntity.ok(service.update(calendar));
    }

    @DeleteMapping(path = CalendarConstants.API_ENDPOINT_CALENDARS_DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> delete(@RequestBody List<UUID> calendarsUuids) {
        service.delete(calendarsUuids);
        return ResponseEntity.ok("Done!");
    }

}
