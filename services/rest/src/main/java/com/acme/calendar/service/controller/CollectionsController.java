package com.acme.calendar.service.controller;

import com.acme.calendar.core.CalendarConstants;
import com.acme.calendar.model.validation.UpdateValidationGroup;
import com.acme.calendar.service.model.collections.Collection;
import com.acme.calendar.service.model.rest.payloads.CollectionCreateRequest;
import com.acme.calendar.service.model.rest.payloads.CollectionUpdateRequest;
import com.acme.calendar.service.model.rest.responses.CollectionResponse;
import com.acme.calendar.service.service.CollectionsService;
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
@RequestMapping(path = CalendarConstants.API_ENDPOINT_PREFIX + CalendarConstants.API_ENDPOINT_COLLECTIONS)
public class CollectionsController {

    CollectionsService service;

    @Autowired
    public CollectionsController(CollectionsService service) {
        this.service = service;
    }

    @PostMapping(path = CalendarConstants.API_ENDPOINT_COLLECTIONS_CREATE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionResponse> create(@RequestBody CollectionCreateRequest collectionRequest) {
        Collection collection = DTOMapper.INSTANCE.toEntity(collectionRequest);
        return ResponseEntity.ok(service.create(collection));
    }
    
    @GetMapping(path = CalendarConstants.API_ENDPOINT_COLLECTIONS_GET_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CollectionResponse>> getAll(Pageable pageable, Sort sort,
                                                           @RequestParam(defaultValue = "false") boolean includeItems,
                                                           @RequestParam(defaultValue = "false") boolean includeNested) {
        return ResponseEntity.ok(service.getAll(pageable,sort,includeItems,includeNested));
    }

    @GetMapping(path = CalendarConstants.API_ENDPOINT_COLLECTIONS_GET_BY_UUID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionResponse> getByUuid(@RequestParam UUID collectionUuid, 
                                                        @RequestParam(defaultValue = "false") boolean includeItems,
                                                        @RequestParam(defaultValue = "false") boolean includeNested) {
        return ResponseEntity.ok(service.getByUuid(collectionUuid,includeItems,includeNested));
    }

    @PutMapping(path = CalendarConstants.API_ENDPOINT_COLLECTIONS_UPDATE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionResponse> update(@Validated(UpdateValidationGroup.class) 
                                                     @RequestBody CollectionUpdateRequest collectionUpdateRequest) {
        Collection collection = DTOMapper.INSTANCE.toEntity(collectionUpdateRequest);
        service.update(collection);
        return ResponseEntity.ok(service.getByUuid(collectionUpdateRequest.getUuid(),true,false));
    }

    @DeleteMapping(path = CalendarConstants.API_ENDPOINT_COLLECTIONS_DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> delete(@RequestBody List<UUID> collectionUuids) {
        service.delete(collectionUuids);
        return ResponseEntity.ok("");
    }

}
