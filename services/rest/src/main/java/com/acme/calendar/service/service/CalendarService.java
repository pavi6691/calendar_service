package com.acme.calendar.service.service;

import com.acme.calendar.service.model.calendar.Calendar;
import com.acme.calendar.service.model.collections.Collection;
import com.acme.calendar.service.repository.PGCCalendarRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class CalendarService extends AbstractService {

    PGCCalendarRepository pgCCalendarRepository;
    @PersistenceContext
    private EntityManager entityManager;


    @Autowired
    public CalendarService(PGCCalendarRepository pgCCalendarRepository) {
        this.pgCCalendarRepository = pgCCalendarRepository;
    }


    public Calendar create(Calendar calendar) {
        UUID uuid;
        if(calendar.getUuid() != null) {
            uuid = calendar.getUuid();
        } else {
            uuid = UUID.randomUUID();
        }
        if(calendar.getMappings() != null) {
            calendar.getMappings().stream().forEach(pc -> {
                if(pc.getParent() != null) {
                    if(pc.getChildOrder() == -1) {
                        pc.setChildOrder(getOrder(pc.getParent().getUuid()));
                    }
                    pc.setCalendar(Calendar.builder().uuid(uuid).build());
                }
            });
        }
        calendar.setUuid(uuid);
        pgCCalendarRepository.save(calendar);
        return getByUuid(calendar.getUuid());
    }
    
    public List<Calendar> getAll(Pageable pageable, Sort sort) {
        List<Calendar> c = pgCCalendarRepository.findAll();
        return c;
    }

    public Calendar getByUuid(UUID uuid) {
        return pgCCalendarRepository.findById(uuid).orElse(null);
    }

    public Calendar update(Calendar cCalendar) {
        return pgCCalendarRepository.save(cCalendar);
    }

    public void delete(List<UUID> cCalendars) {
        pgCCalendarRepository.deleteAllById(cCalendars);
    }

}
