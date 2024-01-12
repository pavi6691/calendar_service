package com.acme.calendar.service.controller;

import com.acme.calendar.core.CalendarConstants;
import com.acme.calendar.model.set.CSet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(path = CalendarConstants.API_ENDPOINT_PREFIX + CalendarConstants.API_ENDPOINT_SETS)
public class CSetController {


    @PostMapping(path = CalendarConstants.API_ENDPOINT_SETS_CREATE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSet> create(@RequestBody CSet cSet) {
        return ResponseEntity.ok(null);
    }

    @GetMapping(path = CalendarConstants.API_ENDPOINT_SETS_GET_ALL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CSet>> getAll(Pageable pageable, Sort sort) {
        return ResponseEntity.ok(null);
    }

    @GetMapping(path = CalendarConstants.API_ENDPOINT_SETS_GET_BY_UUID, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CSet> getByUuid(UUID uuid) {
        return ResponseEntity.ok(null);
    }

    @PutMapping(path = CalendarConstants.API_ENDPOINT_SETS_UPDATE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CSet>> update(@RequestBody CSet cSet) {
        return ResponseEntity.ok(null);
    }

    @DeleteMapping(path = CalendarConstants.API_ENDPOINT_SETS_DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CSet>> delete(@RequestBody List<UUID> cSets) {
        return ResponseEntity.ok(null);
    }

}
