package com.acme.calendar.service.service;

import com.acme.calendar.core.enums.CalendarAPIError;
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

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static com.acme.calendar.service.utils.ExceptionUtil.throwRestError;

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
        if(event.getCreatedInitially() == null) {
            ZonedDateTime zonedDateTime = ZonedDateTime.now();
            event.setCreatedInitially(zonedDateTime);
            event.setLastUpdatedTime(zonedDateTime);
        }
        event.setUuid(UUID.randomUUID());
        return pgCEventRepository.save(event);                  // TODO Fix EntityNotFoundException exceptions thrown when provided calendar uuid does not exist; currently throws stacktrace to client (which should never happen!)
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
        if(event.getLastUpdatedTime() == null) {
            throwRestError(CalendarAPIError.ERROR_ENTRY_HAS_NO_MODIFIED_DATE);
        }
        Event existingEvent = pgCEventRepository.findById(event.getUuid()).orElse(null);
        if(existingEvent == null) {
            throwRestError(CalendarAPIError.ERROR_NOT_EXISTS_UUID, event.getUuid());
        }
        if(!existingEvent.getLastUpdatedTime().equals(event.getLastUpdatedTime())) {
            throwRestError(CalendarAPIError.ERROR_ENTRY_HAS_BEEN_MODIFIED, existingEvent.getLastUpdatedTime());
        }
        event.setLastUpdatedTime(ZonedDateTime.now());
        return pgCEventRepository.save(event);
    }

    public void delete(List<UUID> cEvents) {
        log.debug("{}", LogUtil.method());
        pgCEventRepository.deleteAllById(cEvents);
    }

}
