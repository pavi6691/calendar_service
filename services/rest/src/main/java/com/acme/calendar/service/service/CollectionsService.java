package com.acme.calendar.service.service;

import com.acme.calendar.service.model.collections.Collection;
import com.acme.calendar.service.model.calendar.Calendar;
import com.acme.calendar.service.model.collections.CollectionOrder;
import com.acme.calendar.service.model.event.Event;
import com.acme.calendar.service.model.rest.request.UpdateCollectionRequest;
import com.acme.calendar.service.model.rest.response.RestCollection;
import com.acme.calendar.service.repository.PGCCalendarRepository;
import com.acme.calendar.service.repository.PGCollectionOrderRepository;
import com.acme.calendar.service.repository.PGCollectionsRepository;
import com.acme.calendar.service.utils.DTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class CollectionsService extends AbstractService {

    PGCollectionsRepository pgCSetRepository;
    
    PGCCalendarRepository pgcCalendarRepository;
    
    PGCollectionOrderRepository pgCollectionOrderRepository;
    
    private static final String CALENDAR = "calendar";
    private static final String COLLECTION = "collection";

    @Autowired
    public CollectionsService(PGCollectionsRepository pgCSetRepository,PGCCalendarRepository pgcCalendarRepository,
                              PGCollectionOrderRepository pgCollectionOrderRepository) {
        this.pgCSetRepository = pgCSetRepository;
        this.pgcCalendarRepository = pgcCalendarRepository;
        this.pgCollectionOrderRepository = pgCollectionOrderRepository;
    }


    public RestCollection create(Collection collection) {
        UUID uuid;
        if(collection.getUuid() != null) {
            uuid = collection.getUuid();   
        } else {
            uuid = UUID.randomUUID();
        }
        if(collection.getMappings() != null) {
            collection.getMappings().stream().forEach(pc -> {
                if(pc.getParent() != null) {
                    if(pc.getChildOrder() == -1) {
                        pc.setChildOrder(getOrder(pc.getParent().getUuid()));
                    }
                    pc.setChild(Collection.builder().uuid(uuid).build());
                }
            });
        }
        collection.setUuid(uuid);
        pgCSetRepository.save(collection);
        return getByUuid(collection.getUuid());
    }
    
    public List<RestCollection> getAll(Pageable pageable, Sort sort) {
        Set<UUID> filterChildren = new HashSet<>();
        return pgCSetRepository.findAll().stream()
                .filter(collection -> collection != null && !filterChildren.contains(collection.getUuid()))
                .map(collection -> {
                    RestCollection restCollection = new RestCollection();
                    process(collection,restCollection,filterChildren);
                    return restCollection;
                }).collect(Collectors.toList());
    }

    public RestCollection getByUuid(UUID uuid) {
        Collection collection = pgCSetRepository.findById(uuid).orElse(null);
        if(collection == null) {
            return null;
        }
        RestCollection restCollection = new RestCollection();
        process(collection, restCollection, new HashSet<>());
        return restCollection;
    }
    
    private void process(Collection collection,RestCollection restCollection,Set<UUID> filter) {
        if(collection == null) {
            return;
        }
        filter.add(collection.getUuid());
        restCollection.setUuid(collection.getUuid());
        restCollection.setTitle(collection.getTitle());
        restCollection.setDescription(collection.getDescription());
        Object[] collectionEntries = restCollection.getCollection(collection.getCalendars().size() + collection.getMappings().size());
        for (Calendar c : collection.getCalendars()) {
            c.setType(CALENDAR);
            collectionEntries[c.getOrder()] = c;
        }
        for (CollectionOrder pc : collection.getMappings()) {
            RestCollection nestedCollection = RestCollection.builder().type(COLLECTION).uuid(collection.getUuid())
                    .title(collection.getTitle()).description(collection.getDescription()).build();
            collectionEntries[pc.getChildOrder()] = nestedCollection;
            process(pc.getChild(),nestedCollection,filter);
        }
    }

//    private Map<String,Map<UUID,Object>> getAllItemsFor(Collection collection) {
//        if(collection == null) {
//            return;
//        }
//        Map<String,Map<UUID,Object>> items = new HashMap<>();
//        RestCollection restCollection = new RestCollection();
//        restCollection.setUuid(collection.getUuid());
//        restCollection.setTitle(collection.getTitle());
//        restCollection.setDescription(collection.getDescription());
//        for (Calendar c : collection.getCalendar()) {
//            c.setType(CALENDAR);
//            items.putIfAbsent(CALENDAR,new HashMap<>()).put(c.getUuid(),c);
//        }
//        for (CollectionOrder pc : collection.getParentChildOrders()) {
//            RestCollection nestedCollection = RestCollection.builder().type(COLLECTION).uuid(collection.getUuid())
//                    .title(collection.getTitle()).description(collection.getDescription()).build();
//            items.putIfAbsent(COLLECTION,new HashMap<>()).put(nestedCollection.getUuid(),nestedCollection);
//        }
//    }


    public void update(UUID uuid, UpdateCollectionRequest updateCollectionRequests) {
        Collection existingCollection = pgCSetRepository.findById(uuid).orElse(null);
        if(existingCollection == null) {
            return;
        }
        Map<UUID,CollectionOrder> childCollectionOrders = existingCollection.getMappings().stream().collect(
                Collectors.toMap(co -> co.getChild().getUuid(), Function.identity()));
        Map<UUID,Calendar> childCalendars = existingCollection.getCalendars().stream().collect(
                Collectors.toMap(co -> co.getUuid(), Function.identity()));
        update(existingCollection,updateCollectionRequests,childCollectionOrders,childCalendars);
        pgcCalendarRepository.deleteAllById(childCalendars.values().stream().map(cc -> cc.getUuid()).collect(Collectors.toList()));
        pgCollectionOrderRepository.deleteAllById(childCollectionOrders.values().stream().map(id -> id.getId()).collect(Collectors.toList()));
    }
    @Transactional
    private void update(Collection existingCollection, UpdateCollectionRequest updateCollectionRequests,
                       Map<UUID,CollectionOrder> childCollectionOrders,Map<UUID,Calendar> childCalendars) {
        AtomicInteger order  = new AtomicInteger(0);
        Set<UUID> filterDuplicateJustInCaseFromFrontEnd = new HashSet<>();
        updateCollectionRequests.items().stream().filter(f -> !filterDuplicateJustInCaseFromFrontEnd.contains(f)).forEach(updateCollectionRequest -> {
            filterDuplicateJustInCaseFromFrontEnd.add(updateCollectionRequest.getUuid());
            if(updateCollectionRequest instanceof Calendar) {
                Calendar calendar = (Calendar) updateCollectionRequest;
                calendar.setOrder(order.get());
                if(childCalendars != null && childCalendars.containsKey(calendar.getUuid())) {
                    calendar.setCollection(existingCollection);
                    Calendar existingCalendar = childCalendars.get(calendar.getUuid());
                    calendar.setUuid(existingCalendar.getUuid());
                    DTOMapper.INSTANCE.copy(calendar,existingCalendar);
                } else {
                    if(calendar.getUuid() == null)
                        calendar.setUuid(UUID.randomUUID());
                    calendar.setCollection(existingCollection);
                    pgcCalendarRepository.save(calendar);
                }
                childCalendars.remove(calendar.getUuid());
            } else if(updateCollectionRequest instanceof Collection) {
                Collection collection = (Collection) updateCollectionRequest;
                if(childCollectionOrders != null && childCollectionOrders.containsKey(collection.getUuid())) {
                    CollectionOrder existingChildCollection = childCollectionOrders.get(collection.getUuid());
                    collection.setUuid(existingChildCollection.getChild().getUuid());
                    DTOMapper.INSTANCE.copy(collection,existingChildCollection.getChild());
                    existingChildCollection.setChildOrder(order.get());
                } else {
                    if(collection.getUuid() == null)
                        collection.setUuid(UUID.randomUUID());
                    collection.getMappings()
                            .add(CollectionOrder.builder().parent(existingCollection).child(collection).childOrder(order.get()).build());
                    pgCSetRepository.save(collection);
                }
                childCollectionOrders.remove(collection.getUuid());
            }
            order.getAndIncrement();
        });
        pgCSetRepository.save(existingCollection);
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
