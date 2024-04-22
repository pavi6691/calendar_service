package com.acme.calendar.service.controller;

import com.acme.calendar.core.CalendarConstants;
import com.acme.calendar.service.exceptions.validations.CreateValidationGroup;
import com.acme.calendar.service.exceptions.validations.UpdateValidationGroup;
import com.acme.calendar.service.model.calendar.Calendar;
import com.acme.calendar.service.model.calendar.CalendarMapping;
import com.acme.calendar.service.model.collections.Collection;
import com.acme.calendar.service.model.rest.payloads.CalendarRequest;
import com.acme.calendar.service.model.rest.responses.CalendarResponse;
import com.acme.calendar.service.service.CalendarService;
import com.acme.calendar.service.utils.DTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
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
    @Operation(summary = "Create Calendar", description = "Register Calendar", tags = { "Calendars" })
    @PostMapping(path = CalendarConstants.API_ENDPOINT_CALENDARS_CREATE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CalendarResponse> create(Authentication authentication, @PathVariable UUID collectionUuid,
        @Validated(CreateValidationGroup.class) @RequestBody CalendarRequest calendarRequest) {
        Calendar calendar = DTOMapper.INSTANCE.toEntity(calendarRequest);
        calendar.setMappings((Set.of(CalendarMapping.builder().parent(Collection.builder().uuid(collectionUuid).build()).build())));
        return ResponseEntity.ok(service.create(authentication, calendar));
    }
    @Operation(summary = "Get all Calendars", description = "Get all Calendars", tags = { "Calendars" })
    @GetMapping(path = CalendarConstants.API_ENDPOINT_CALENDARS_GET_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CalendarResponse>> getAll(Authentication authentication, @RequestParam(defaultValue = "false") boolean includeEvents, Pageable pageable, Sort sort) {
        return ResponseEntity.ok(service.getAll(authentication, pageable, sort, includeEvents));
    }
    @Operation(summary = "Get Calendar by id", description = "Get Calendar", tags = { "Calendars" })
    @GetMapping(path = CalendarConstants.API_ENDPOINT_CALENDARS_GET_BY_UUID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CalendarResponse> getByUuid(Authentication authentication, @RequestParam UUID calendarUuid, @RequestParam(defaultValue = "false") boolean includeEvents) {
        return ResponseEntity.ok(service.getByUuid(authentication, calendarUuid,includeEvents));
    }
    @Operation(summary = "Update Calendar", description = "Update Calendar", tags = { "Calendars" })
    @PutMapping(path = CalendarConstants.API_ENDPOINT_CALENDARS_UPDATE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CalendarResponse> update(@Validated(UpdateValidationGroup.class) @RequestBody CalendarRequest calendarRequest) throws Exception {
        Calendar calendar = DTOMapper.INSTANCE.toEntity(calendarRequest);
        return ResponseEntity.ok(service.update(calendar));
    }
    @Operation(summary = "Delete Calendar", description = "Delete Calendar", tags = { "Calendars" })
    @DeleteMapping(path = CalendarConstants.API_ENDPOINT_CALENDARS_DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> delete(@RequestBody List<UUID> calendarsUuids) {
        service.delete(calendarsUuids);
        return ResponseEntity.ok("Done!");
    }
}
