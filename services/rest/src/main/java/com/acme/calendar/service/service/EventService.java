package com.acme.calendar.service.service;

import com.acme.calendar.core.util.LogUtil;
import com.acme.calendar.service.model.event.Event;
import com.acme.calendar.service.repository.PGCEventRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
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
        log.debug("{}", LogUtil.method());
        event.setUuid(UUID.randomUUID());
        return pgCEventRepository.save(event);
    }
    
    public List<Event> getAll(Pageable pageable, Sort sort) {
        log.debug("{}", LogUtil.method());
        List<Event> e = pgCEventRepository.findAll();
        return e;
    }

    public Event getByUuid(UUID uuid) {
        log.debug("{}", LogUtil.method());
        return pgCEventRepository.findById(uuid).orElse(null);
    }

    public Event update(Event event) {
        log.debug("{}", LogUtil.method());
        return pgCEventRepository.save(event);
    }

    public void delete(List<UUID> cEvents) {
        log.debug("{}", LogUtil.method());
        pgCEventRepository.deleteAllById(cEvents);
    }

}
