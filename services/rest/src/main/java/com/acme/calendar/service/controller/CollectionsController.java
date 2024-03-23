package com.acme.calendar.service.controller;

import com.acme.calendar.core.CalendarConstants;
import com.acme.calendar.model.validation.UpdateValidationGroup;
import com.acme.calendar.service.model.collections.Collection;
import com.acme.calendar.service.service.CollectionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

import static com.acme.calendar.core.CalendarConstants.API_PATH_COLLECTION_UUID;


@RestController
@RequestMapping(path = CalendarConstants.API_ENDPOINT_PREFIX + CalendarConstants.API_ENDPOINT_COLLECTIONS)
public class CollectionsController {

    CollectionsService service;

    @Autowired
    public CollectionsController(CollectionsService service) {
        this.service = service;
    }

    @PostMapping(path = CalendarConstants.API_ENDPOINT_COLLECTIONS_CREATE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection> create(@RequestBody Collection collection) {
        return ResponseEntity.ok(service.create(collection));
    }

    @GetMapping(path = CalendarConstants.API_ENDPOINT_COLLECTIONS_GET_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Collection>> getAll(Pageable pageable, Sort sort) {
        return ResponseEntity.ok(service.getAll(pageable,sort));
    }

    @GetMapping(path = CalendarConstants.API_ENDPOINT_COLLECTIONS_GET_BY_UUID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection> getByUuid(@PathVariable(name = API_PATH_COLLECTION_UUID) UUID uuid) {
        return ResponseEntity.ok(service.getByUuid(uuid));
    }

    @PutMapping(path = CalendarConstants.API_ENDPOINT_COLLECTIONS_UPDATE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection> update(@Validated(UpdateValidationGroup.class) @RequestBody Collection updateCollectionRequest) {
        service.update(updateCollectionRequest);
        return ResponseEntity.ok(service.getByUuid(updateCollectionRequest.getUuid()));
    }

    @DeleteMapping(path = CalendarConstants.API_ENDPOINT_COLLECTIONS_DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> delete(@RequestBody List<UUID> collectionUuids) {
        service.delete(collectionUuids);
        return ResponseEntity.ok("");
    }

}
