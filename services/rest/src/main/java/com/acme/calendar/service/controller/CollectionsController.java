package com.acme.calendar.service.controller;

import com.acme.calendar.core.CalendarConstants;
import com.acme.calendar.service.exceptions.validations.CreateValidationGroup;
import com.acme.calendar.service.exceptions.validations.UpdateValidationGroup;
import com.acme.calendar.service.model.collections.Collection;
import com.acme.calendar.service.model.collections.CollectionMapping;
import com.acme.calendar.service.model.collections.MappingPK;
import com.acme.calendar.service.model.rest.payloads.CollectionRequest;
import com.acme.calendar.service.model.rest.responses.CollectionResponse;
import com.acme.calendar.service.service.CollectionsService;
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
    
    @Operation(summary = "Create Collections", description = "Create Collections", tags = { "Collections" })
    @PostMapping(path = CalendarConstants.API_ENDPOINT_COLLECTIONS_CREATE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionResponse> create(Authentication authentication, @Validated(CreateValidationGroup.class) @RequestBody CollectionRequest collectionRequest) {
        Collection collection = DTOMapper.INSTANCE.toEntity(collectionRequest);
        return ResponseEntity.ok(service.create(authentication, collection));
    }
    @Operation(summary = "Create Child Collections", description = "Create Child Collections", tags = { "Collections" })
    @PostMapping(path = CalendarConstants.API_ENDPOINT_COLLECTIONS_CREATE_CHILD, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionResponse> create(Authentication authentication, @PathVariable(required = false) UUID collectionUuid,
        @Validated(CreateValidationGroup.class) @RequestBody CollectionRequest collectionRequest) {
        Collection collection = DTOMapper.INSTANCE.toEntity(collectionRequest);
        if(collectionUuid != null) {
            collection.getCollectionMappings()
                .add(CollectionMapping.builder().parent(Collection.builder()
                    .uuid(collectionUuid).build()).build());
        }
        return ResponseEntity.ok(service.create(authentication, collection));
    }
    @Operation(summary = "Get all Collections", description = "Get all Collections", tags = { "Collections" })
    @GetMapping(path = CalendarConstants.API_ENDPOINT_COLLECTIONS_GET_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CollectionResponse>> getAll( Authentication authentication, Pageable pageable, Sort sort,
        @RequestParam(defaultValue = "false") boolean includeItems,
        @RequestParam(defaultValue = "false") boolean includeNested) {
        return ResponseEntity.ok(service.getAll(authentication, pageable,sort,includeItems,includeNested));
    }
    @Operation(summary = "Get Collection by uuid", description = "Get Collection", tags = { "Collections" })
    @GetMapping(path = CalendarConstants.API_ENDPOINT_COLLECTIONS_GET_BY_UUID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionResponse> getByUuid(Authentication authentication, @RequestParam UUID collectionUuid,
        @RequestParam(defaultValue = "false") boolean includeItems,
        @RequestParam(defaultValue = "false") boolean includeNested) {
        return ResponseEntity.ok(service.getByUuid(authentication, collectionUuid,includeItems,includeNested));
    }
    @Operation(summary = "Update Collections", description = "Update Collection", tags = { "Collections" })
    @PutMapping(path = CalendarConstants.API_ENDPOINT_COLLECTIONS_UPDATE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionResponse> update(Authentication authentication, @Validated(UpdateValidationGroup.class) @RequestBody CollectionRequest collectionRequest) {
        Collection collection = DTOMapper.INSTANCE.toEntity(collectionRequest);
        service.update(collection);
        return ResponseEntity.ok(service.getByUuid(authentication, collectionRequest.getUuid(),true,false));
    }
    @Operation(summary = "Delete Collections", description = "Delete Collection", tags = { "Collections" })
    @DeleteMapping(path = CalendarConstants.API_ENDPOINT_COLLECTIONS_DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> delete(@RequestBody List<UUID> collectionUuids) {
        service.delete(collectionUuids);
        return ResponseEntity.ok("");
    }

}
