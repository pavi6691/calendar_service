package com.acme.calendar.service.service;

import com.acme.calendar.service.model.calendar.CalendarMapping;
import com.acme.calendar.service.model.collections.Collection;
import com.acme.calendar.service.model.calendar.Calendar;
import com.acme.calendar.service.model.collections.CollectionMapping;
import com.acme.calendar.service.model.collections.MappingPK;
import com.acme.calendar.service.model.event.Event;
import com.acme.calendar.service.repository.PGCCalendarRepository;
import com.acme.calendar.service.repository.PGCalendarMappingRepo;
import com.acme.calendar.service.repository.PGCollectionMappingRepo;
import com.acme.calendar.service.repository.PGCollectionsRepository;
import com.acme.calendar.service.utils.DTOMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;


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


    public Collection create(Collection collection) {
        if(collection.getUuid() != null) {
            collection.setUuid(collection.getUuid());   
        } else {
            collection.setUuid(UUID.randomUUID());
        }
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
        return getByUuid(collection.getUuid());
    }
    
    public List<Collection> getAll(Pageable pageable, Sort sort) {
        Set<UUID> filterChildren = new HashSet<>();
        return pgCSetRepository.findAll().stream()
                .filter(collection -> collection != null && !filterChildren.contains(collection.getUuid()))
                .map(collection -> {
                    Collection restCollection = new Collection();
                    process(collection,restCollection,filterChildren);
                    return restCollection;
                }).collect(Collectors.toList());
    }

    public Collection getByUuid(UUID uuid) {
        Collection collection = pgCSetRepository.findById(uuid).orElse(null);
        if(collection == null) {
            return null;
        }
        Collection restCollection = new Collection();
        process(collection, restCollection, new HashSet<>());
        return restCollection;
    }
    
    private void process(Collection collection,Collection restCollection,Set<UUID> filter) {
        if(collection == null) {
            return;
        }
        filter.add(collection.getUuid());
        restCollection.setUuid(collection.getUuid());
        restCollection.setTitle(collection.getTitle());
        restCollection.setDescription(collection.getDescription());
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
    public void update(Collection updateCollectionRequests) {
        Collection existingCollection = entityManager.find(Collection.class,updateCollectionRequests.getUuid());
        if(existingCollection == null) {
            return;
        }
        // Copy existing mappings and orders into temporary maps
        Map<UUID, CollectionMapping> orphanCollections = existingCollection.getCollectionMappings().stream().collect(
                Collectors.toMap(co -> co.getChild().getUuid(), Function.identity()));
        Map<UUID,CalendarMapping> orphanCalendar = existingCollection.getCalendarMapping().stream()
                .collect(Collectors.toMap(co -> co.getCalendar().getUuid(), Function.identity()));
        
        DTOMapper.INSTANCE.copy(updateCollectionRequests,existingCollection);
        
        // provision updates
        update(existingCollection,updateCollectionRequests,orphanCollections,orphanCalendar);
        
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

    
    private void update(Collection existingCollection,
                        Collection updateCollectionRequests,
                        Map<UUID, CollectionMapping> orphanCollections,
                        Map<UUID,CalendarMapping> orphanCalendar) {
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
                CalendarMapping calendarMapping;
                if(orphanCalendar != null && orphanCalendar.containsKey(calendarRequest.getUuid())) {
                    calendarMapping = orphanCalendar.get(calendarRequest.getUuid());
                    calendarRequest.setUuid(calendarMapping.getCalendar().getUuid());
                    DTOMapper.INSTANCE.copy(calendarRequest,calendarMapping.getCalendar());
                    calendarMapping.setChildOrder(order.get());
                } else {
                    if(calendarRequest.getUuid() == null)
                        calendarRequest.setUuid(UUID.randomUUID());
                    calendarMapping = CalendarMapping.builder()
                            .id(MappingPK.builder().parentId(existingCollection.getUuid()).childId(calendarRequest.getUuid()).build())
                            .calendar(calendarRequest).parent(existingCollection).childOrder(order.get()).build();
                }
                existingCollection.getCalendarMapping().add(calendarMapping);
                orphanCalendar.remove(calendarRequest.getUuid());
            } else if(updateCollectionRequest instanceof Collection) {
                Collection collectionRequest = (Collection) updateCollectionRequest;
                CollectionMapping existingChildCollection;
                if(orphanCollections != null && orphanCollections.containsKey(collectionRequest.getUuid())) {
                    existingChildCollection = orphanCollections.get(collectionRequest.getUuid());
                    collectionRequest.setUuid(existingChildCollection.getChild().getUuid());
                    DTOMapper.INSTANCE.copy(collectionRequest,existingChildCollection.getChild());
                    existingChildCollection.setChildOrder(order.get());
                } else {
                    if(collectionRequest.getUuid() == null)
                        collectionRequest.setUuid(UUID.randomUUID());
                    existingChildCollection = get(existingCollection,collectionRequest,order);
                }
                existingCollection.getCollectionMappings().add(existingChildCollection);
                orphanCollections.remove(collectionRequest.getUuid());
            }
            order.getAndIncrement();
        });
    }
    
    private void itemsToMapping(Collection existingCollection,Collection collection,AtomicInteger order) {
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
        return CalendarMapping.builder()
                .id(MappingPK.builder().parentId(existingCollection.getUuid()).childId(calendar.getUuid()).build())
                .parent(existingCollection).calendar(calendar).childOrder(order.get()).build();
    }

    private CollectionMapping get(Collection existingCollection, Collection collection, AtomicInteger order) {
        return CollectionMapping.builder()
                .id(MappingPK.builder().parentId(existingCollection.getUuid()).childId(collection.getUuid()).build())
                .parent(existingCollection).child(collection).childOrder(order.get()).build();
    }

    public void delete(List<UUID> cSets) {
        pgCSetRepository.deleteAllById(cSets);
    }

    public List<Event> findEventsByCollectionId(UUID collectionId) {
        return entityManager.createQuery(
                        "SELECT e FROM Event e " +
                                "JOIN e.calendar c " +
                                "JOIN c.collection co " +
                                "WHERE co.uuid = :collectionId", Event.class)
                .setParameter("collectionId", collectionId)
                .getResultList();
    }

    public String[] getCollectionAndCalendarUuids(UUID collectionId) {
        String queryString = "SELECT e.collectionAndCalendarUuids FROM collection e WHERE e.uuid = :collectionId";
        return entityManager.createQuery(queryString, String[].class)
                .setParameter("collectionId", collectionId)
                .getSingleResult();
    }

    public Map<UUID, Collection> getNestedEntities(String[] collectionAndCalendarUuids) {
        String queryString = "SELECT n FROM collection n WHERE n.uuid IN :collectionAndCalendarUuids";
        return entityManager.createQuery(queryString, Collection.class)
                .setParameter("nestedUuids", collectionAndCalendarUuids)
                .getResultList().stream().collect(Collectors.toMap(Collection::getUuid, collection -> collection));
    }

    public Map<UUID, Calendar> getCalendarEntities(String[] collectionAndCalendarUuids) {
        String queryString = "SELECT n FROM calendar n WHERE n.uuid IN :collectionAndCalendarUuids";
        return entityManager.createQuery(queryString, Calendar.class)
                .setParameter("nestedUuids", collectionAndCalendarUuids)
                .getResultList().stream().collect(Collectors.toMap(Calendar::getUuid, cal -> cal));
    }
}
