package com.acme.calendar.service.service;

import com.acme.calendar.core.enums.CalendarAPIError;
import com.acme.calendar.core.util.LogUtil;
import com.acme.calendar.service.model.IEntry;
import com.acme.calendar.service.model.calendar.CalendarMapping;
import com.acme.calendar.service.model.collections.Collection;
import com.acme.calendar.service.model.calendar.Calendar;
import com.acme.calendar.service.model.collections.CollectionMapping;
import com.acme.calendar.service.model.collections.MappingPK;
import com.acme.calendar.service.model.event.Event;
import com.acme.calendar.service.model.rest.responses.CollectionResponse;
import com.acme.calendar.service.repository.PGCCalendarRepository;
import com.acme.calendar.service.repository.PGCalendarMappingRepo;
import com.acme.calendar.service.repository.PGCollectionMappingRepo;
import com.acme.calendar.service.repository.PGCollectionsRepository;
import com.acme.calendar.service.utils.DTOMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.acme.calendar.service.utils.ExceptionUtil.throwRestError;


@Slf4j
@Service
public class CollectionsService extends AbstractService {

    PGCollectionsRepository pgCSetRepository;
    
    PGCCalendarRepository pgcCalendarRepository;

    PGCollectionMappingRepo pgCollectionMappingRepo;
    PGCalendarMappingRepo pgCalendarMappingRepo;
    
    @PersistenceContext
    EntityManager entityManager;
    
    private static final String CALENDAR = "calendar";
    private static final String COLLECTION = "collection";

    @Autowired
    public CollectionsService(PGCollectionsRepository pgCSetRepository,PGCCalendarRepository pgcCalendarRepository,
                              PGCollectionMappingRepo pgCollectionMappingRepo,PGCalendarMappingRepo pgCalendarMappingRepo) {
        this.pgCSetRepository = pgCSetRepository;
        this.pgcCalendarRepository = pgcCalendarRepository;
        this.pgCollectionMappingRepo = pgCollectionMappingRepo;
        this.pgCalendarMappingRepo = pgCalendarMappingRepo;
    }


    public CollectionResponse create(Collection collection) {
        log.debug("{}", LogUtil.method());
        build(collection);
        AtomicReference<Collection> nested = new AtomicReference<>();
        if(collection.getCollectionMappings() != null) {
            collection.getCollectionMappings().stream().forEach(pc -> {
                if(pc.getParent() != null) {
                    nested.set(pgCSetRepository.findById(pc.getParent().getUuid()).orElse(null));
                    if(nested.get() == null) {
                        return;
                    }
                    if(pc.getChildOrder() == -1) {
                        pc.setChildOrder(getOrder(pc.getParent().getUuid()));
                    }
                    pc.setChild(collection);
                    nested.get().getCollectionMappings().add(pc);
                }
            });
        }
        if(nested.get() != null && !nested.get().getCollectionMappings().isEmpty()) {
            pgCSetRepository.save(nested.get());   
        } else {
            pgCSetRepository.save(collection);
        }
        return getByUuid(collection.getUuid(),false,false);
    }
    
    public List<CollectionResponse> getAll(Pageable pageable, Sort sort, boolean includeItems,boolean includeNested) {
        log.debug("{}", LogUtil.method());
        Set<UUID> filterChildren = new HashSet<>();
        return pgCSetRepository.findAll().stream()
                .filter(collection -> collection != null && !filterChildren.contains(collection.getUuid()))
                .map(collection -> {
                    Collection restCollection = new Collection();
                    process(collection,restCollection,filterChildren,includeNested);
                    if(includeItems) {
                        return DTOMapper.INSTANCE.toCollectionResponseWithItems(restCollection);
                    } else {
                        return DTOMapper.INSTANCE.toCollectionResponseWithoutItems(restCollection);   
                    }
                }).collect(Collectors.toList());
    }

    public CollectionResponse getByUuid(UUID uuid, boolean includeItems,boolean includeNested) {
        log.debug("{}", LogUtil.method());
        Collection collection = pgCSetRepository.findById(uuid).orElse(null);
        if(collection == null) {
            throwRestError(CalendarAPIError.ERROR_NOT_EXISTS_UUID, uuid);
        }
        Collection restCollection = new Collection();
        process(collection, restCollection, new HashSet<>(),includeNested);
        if(includeItems) {
            return DTOMapper.INSTANCE.toCollectionResponseWithItems(restCollection);   
        }
        return DTOMapper.INSTANCE.toCollectionResponseWithoutItems(restCollection);
    }

    private void process(Collection collection,Collection restCollection,Set<UUID> filter, boolean includeNested) {
        log.debug("{}", LogUtil.method());
        if(collection == null) {
            return;
        }
        process(collection,restCollection,filter);
        if(!includeNested) {
            for(IEntry entry : restCollection.getItems()) {
                if(entry instanceof Calendar) {
                    ((Calendar) entry).setEvents(new ArrayList<>());
                } else if(entry instanceof Collection) {
                    ((Collection) entry).setItems(null);
                }
            }
        }
    }
    
    private void process(Collection collection,Collection restCollection,Set<UUID> filter) {
        log.debug("{}", LogUtil.method());
        if(collection == null) {
            return;
        }
        filter.add(collection.getUuid());
        DTOMapper.INSTANCE.copy(collection,restCollection);
        Object[] collectionEntries = restCollection.getCollection(collection.getCalendarMapping().size() + collection.getCollectionMappings().size());
        collection.getCalendarMapping().stream().forEach(c -> {
            c.getCalendar().setType(CALENDAR);
            collectionEntries[c.getChildOrder()] = c.getCalendar(); 
        });
        for (CollectionMapping pc : collection.getCollectionMappings()) {
            Collection nestedCollection = Collection.builder().type(COLLECTION).uuid(collection.getUuid())
                    .title(collection.getTitle()).description(collection.getDescription()).build();
            collectionEntries[pc.getChildOrder()] = nestedCollection;
            process(pc.getChild(),nestedCollection,filter);
        }
    }
    
    @Transactional
    public void update(Collection updateCollectionRequests) {    // TODO Does not update `lastUpdatedTime` to current time
        log.debug("{}", LogUtil.method());
        updateCollectionRequests.setCollectionMappings(new HashSet<>());
        updateCollectionRequests.setCalendarMapping(new HashSet<>());
        if(updateCollectionRequests.getLastUpdatedTime() == null) {
            throwRestError(CalendarAPIError.ERROR_ENTRY_HAS_NO_MODIFIED_DATE);
        }
        Collection existingCollection = entityManager.find(Collection.class,updateCollectionRequests.getUuid());
        if(existingCollection == null) {
            throwRestError(CalendarAPIError.ERROR_NOT_EXISTS_UUID, updateCollectionRequests.getUuid());
        }
        if(!existingCollection.getLastUpdatedTime().equals(updateCollectionRequests.getLastUpdatedTime())) {
            throwRestError(CalendarAPIError.ERROR_ENTRY_HAS_BEEN_MODIFIED,existingCollection.getUuid(), existingCollection.getLastUpdatedTime());
        }
        // Copy existing mappings and orders into temporary maps
        Map<UUID, CollectionMapping> orphanCollections = existingCollection.getCollectionMappings().stream().collect(
                Collectors.toMap(co -> co.getChild().getUuid(), Function.identity()));
        Map<UUID,CalendarMapping> orphanCalendar = existingCollection.getCalendarMapping().stream()
                .collect(Collectors.toMap(co -> co.getCalendar().getUuid(), Function.identity()));
        ZonedDateTime updatedDateTime = ZonedDateTime.now();
        if(!existingCollection.equals(updateCollectionRequests) || 
                updateCollectionRequests.getItems().length != 
                        (existingCollection.getCollectionMappings().size() + existingCollection.getCalendarMapping().size())) {
            updateCollectionRequests.setLastUpdatedTime(updatedDateTime);
        } else {
            Collection restCollection = new Collection();
            process(existingCollection, restCollection, new HashSet<>());
            if(!isOrderSame(updateCollectionRequests.getItems(),restCollection.getItems())) {
                updateCollectionRequests.setLastUpdatedTime(updatedDateTime);    
            }
        }
        
        DTOMapper.INSTANCE.copy(updateCollectionRequests,existingCollection);
        
        // provision updates
        update(existingCollection,updateCollectionRequests,orphanCollections,orphanCalendar,updatedDateTime);
        
        existingCollection.getCollectionMappings().removeAll(orphanCollections.values());
        
        existingCollection.getCalendarMapping().removeAll(orphanCalendar.values());
        
        // Update existing and new collections
        pgCollectionMappingRepo.saveAll(existingCollection.getCollectionMappings());

        // Remove orphaned collection mappings
        pgCollectionMappingRepo.deleteAll(orphanCollections.values());

        // Update existing and new calendars
        pgCalendarMappingRepo.saveAll(existingCollection.getCalendarMapping());

        // Remove orphaned calendar mappings
        pgCalendarMappingRepo.deleteAll(orphanCalendar.values());
    }

    private boolean isOrderSame(IEntry[] items1, IEntry[] items2) {
        for(int i = 0; i <items1.length; i++) {
            if(items1[i].getUuid() == null || !items1[i].getUuid().equals(items2[i].getUuid())) {
                return false;
            }
        }
        return true;
    }
    
    private void update(Collection existingCollection,
                        Collection updateCollectionRequests,
                        Map<UUID, CollectionMapping> orphanCollections,
                        Map<UUID,CalendarMapping> orphanCalendar,
                        ZonedDateTime updatedDateTime) {
        log.debug("{}", LogUtil.method());
        if(updateCollectionRequests.getItems() == null) {
            return;
        }
        AtomicInteger order  = new AtomicInteger(0);
        Set<UUID> filterDuplicateJustInCaseFromFrontEnd = new HashSet<>();
        Arrays.stream(updateCollectionRequests.getItems()).filter(f -> !filterDuplicateJustInCaseFromFrontEnd.contains(f))
                .forEach(updateCollectionRequest -> {
            filterDuplicateJustInCaseFromFrontEnd.add(updateCollectionRequest.getUuid());
            if(updateCollectionRequest instanceof Calendar) {
                Calendar calendarRequest = (Calendar) updateCollectionRequest;
                CalendarMapping existingCalendarMapping;
                if(orphanCalendar != null && orphanCalendar.containsKey(calendarRequest.getUuid())) {
                    existingCalendarMapping = orphanCalendar.get(calendarRequest.getUuid());
                    calendarRequest.setUuid(existingCalendarMapping.getCalendar().getUuid());
                    if(!existingCalendarMapping.getCalendar().getLastUpdatedTime().equals(calendarRequest.getLastUpdatedTime())) {
                        throwRestError(CalendarAPIError.ERROR_ENTRY_HAS_BEEN_MODIFIED,existingCalendarMapping.getCalendar().getUuid(), existingCalendarMapping.getCalendar().getLastUpdatedTime());
                    }
                    if(!calendarRequest.equals(existingCalendarMapping.getCalendar())) {
                        calendarRequest.setLastUpdatedTime(updatedDateTime);
                        existingCollection.setLastUpdatedTime(updatedDateTime);
                    }
                    DTOMapper.INSTANCE.copy(calendarRequest,existingCalendarMapping.getCalendar());
                    existingCalendarMapping.setChildOrder(order.get());
                } else {
                    build(calendarRequest);
                    calendarRequest.setLastUpdatedTime(updatedDateTime);
                    existingCalendarMapping = CalendarMapping.builder()
                            .id(MappingPK.builder().parentId(existingCollection.getUuid()).childId(calendarRequest.getUuid()).build())
                            .calendar(calendarRequest).parent(existingCollection).childOrder(order.get()).build();
                }
                existingCollection.getCalendarMapping().add(existingCalendarMapping);
                orphanCalendar.remove(calendarRequest.getUuid());
            } else if(updateCollectionRequest instanceof Collection) {
                Collection collectionRequest = (Collection) updateCollectionRequest;
                CollectionMapping existingChildCollection;
                if(orphanCollections != null && orphanCollections.containsKey(collectionRequest.getUuid())) {
                    existingChildCollection = orphanCollections.get(collectionRequest.getUuid());
                    collectionRequest.setUuid(existingChildCollection.getChild().getUuid());
                    if(!existingChildCollection.getChild().getLastUpdatedTime().equals(collectionRequest.getLastUpdatedTime())) {
                        throwRestError(CalendarAPIError.ERROR_ENTRY_HAS_BEEN_MODIFIED,existingChildCollection.getChild().getUuid(), existingChildCollection.getChild().getLastUpdatedTime());
                    }
                    if(!collectionRequest.equals(existingChildCollection.getChild())) {
                        collectionRequest.setLastUpdatedTime(updatedDateTime);
                        existingCollection.setLastUpdatedTime(updatedDateTime);
                    }
                    DTOMapper.INSTANCE.copy(collectionRequest,existingChildCollection.getChild());
                    existingChildCollection.setChildOrder(order.get());
                } else {
                    build(collectionRequest);
                    collectionRequest.setLastUpdatedTime(updatedDateTime);
                    existingChildCollection = get(existingCollection,collectionRequest,order);
                }
                existingCollection.getCollectionMappings().add(existingChildCollection);
                orphanCollections.remove(collectionRequest.getUuid());
            }
            order.getAndIncrement();
        });
    }
    
    private void itemsToMapping(Collection existingCollection,Collection collection,AtomicInteger order) {
        log.debug("{}", LogUtil.method());
        if(collection.getItems() != null) {
            Arrays.stream(collection.getItems()).forEach(item -> {
                if (item instanceof Calendar) {
                    Calendar calendar = (Calendar) item;
                    if(calendar.getUuid() == null)
                        calendar.setUuid(UUID.randomUUID());
                    existingCollection.getCalendarMapping().add(get(collection, calendar, order));
                } else if (item instanceof Collection) {
                    Collection collectionItem = (Collection) item;
                    if(collectionItem.getUuid() == null)
                        collectionItem.setUuid(UUID.randomUUID());
                    existingCollection.getCollectionMappings().add(get(collection, collectionItem, order));
                    itemsToMapping(collection,collectionItem,order);
                }
            });
        }
    }
    
    private CalendarMapping get(Collection existingCollection, Calendar calendar, AtomicInteger order) {
        log.debug("{}", LogUtil.method());
        return CalendarMapping.builder()
                .id(MappingPK.builder().parentId(existingCollection.getUuid()).childId(calendar.getUuid()).build())
                .parent(existingCollection).calendar(calendar).childOrder(order.get()).build();
    }

    private CollectionMapping get(Collection existingCollection, Collection collection, AtomicInteger order) {
        log.debug("{}", LogUtil.method());
        return CollectionMapping.builder()
                .id(MappingPK.builder().parentId(existingCollection.getUuid()).childId(collection.getUuid()).build())
                .parent(existingCollection).child(collection).childOrder(order.get()).build();
    }

    public void delete(List<UUID> cSets) {
        log.debug("{}", LogUtil.method());
        pgCSetRepository.deleteAllById(cSets);
    }

    public List<Event> findEventsByCollectionId(UUID collectionId) {
        log.debug("{}", LogUtil.method());
        return entityManager.createQuery(
                        "SELECT e FROM Event e " +
                                "JOIN e.calendar c " +
                                "JOIN c.collection co " +
                                "WHERE co.uuid = :collectionId", Event.class)
                .setParameter("collectionId", collectionId)
                .getResultList();
    }

    public String[] getCollectionAndCalendarUuids(UUID collectionId) {
        log.debug("{}", LogUtil.method());
        String queryString = "SELECT e.collectionAndCalendarUuids FROM collection e WHERE e.uuid = :collectionId";
        return entityManager.createQuery(queryString, String[].class)
                .setParameter("collectionId", collectionId)
                .getSingleResult();
    }

    public Map<UUID, Collection> getNestedEntities(String[] collectionAndCalendarUuids) {
        log.debug("{}", LogUtil.method());
        String queryString = "SELECT n FROM collection n WHERE n.uuid IN :collectionAndCalendarUuids";
        return entityManager.createQuery(queryString, Collection.class)
                .setParameter("nestedUuids", collectionAndCalendarUuids)
                .getResultList().stream().collect(Collectors.toMap(Collection::getUuid, collection -> collection));
    }

    public Map<UUID, Calendar> getCalendarEntities(String[] collectionAndCalendarUuids) {
        log.debug("{}", LogUtil.method());
        String queryString = "SELECT n FROM calendar n WHERE n.uuid IN :collectionAndCalendarUuids";
        return entityManager.createQuery(queryString, Calendar.class)
                .setParameter("nestedUuids", collectionAndCalendarUuids)
                .getResultList().stream().collect(Collectors.toMap(Calendar::getUuid, cal -> cal));
    }
}
