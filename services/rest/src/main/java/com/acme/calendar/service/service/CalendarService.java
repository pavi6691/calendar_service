package com.acme.calendar.service.service;

import com.acme.calendar.service.model.calendar.Calendar;
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
        calendar.setUuid(UUID.randomUUID());
        if(calendar.getCollection() != null) {
            calendar.setOrder(getOrder(calendar.getCollection().getUuid()));
        } else {
            calendar.setOrder(0);
        }
        return pgCCalendarRepository.save(calendar);
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
