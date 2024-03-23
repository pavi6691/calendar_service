package com.acme.calendar.service.service;

import com.acme.calendar.core.enums.CalendarAPIError;
import com.acme.calendar.core.util.LogUtil;
import com.acme.calendar.service.model.event.Event;
import com.acme.calendar.service.repository.EventRepository;
import com.acme.calendar.service.repository.PGCEventRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
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
    private EventRepository eventRepository;

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

    public List<Event> getEventsBetweenDates(UUID calendarGuid, ZonedDateTime startDate, ZonedDateTime endDate) {
        try {
            List<Event> events =  eventRepository.findByStartTimeBetweenAndCalendarUuid(startDate, endDate, calendarGuid);
            if (events.isEmpty()) {
                log.info("No events present between {} and {} for calendar with GUID {}", startDate, endDate, calendarGuid);
                return Collections.emptyList();
            }
            return events;
        } catch (Exception e) {
            throwRestError(CalendarAPIError.ERROR_FETCHING_EVENTS, e.getMessage());
        }
        return null;
    }
}
