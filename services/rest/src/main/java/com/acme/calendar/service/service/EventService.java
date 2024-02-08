package com.acme.calendar.service.service;

import com.acme.calendar.service.model.event.Event;
import com.acme.calendar.service.repository.PGCEventRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
public class EventService {

    PGCEventRepository pgCEventRepository;
    @PersistenceContext
    private EntityManager entityManager;


    @Autowired
    public EventService(PGCEventRepository pgCEventRepository) {
        this.pgCEventRepository = pgCEventRepository;
    }


    public Event create(Event event) {
        event.setUuid(UUID.randomUUID());
        return pgCEventRepository.save(event);
    }
    
    public List<Event> getAll(Pageable pageable, Sort sort) {
        List<Event> e = pgCEventRepository.findAll();
        return e;
    }

    public Event getByUuid(UUID uuid) {
        return pgCEventRepository.findById(uuid).orElse(null);
    }

    public Event update(Event event) {
        return pgCEventRepository.save(event);
    }

    public void delete(List<UUID> cEvents) {
        pgCEventRepository.deleteAllById(cEvents);
    }

}
