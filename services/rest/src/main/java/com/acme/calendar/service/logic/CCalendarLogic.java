package com.acme.calendar.service.logic;

import com.acme.calendar.model.calendar.CCalendar;
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
public class CCalendarLogic {

    PGCCalendarRepository pgCCalendarRepository;
    @PersistenceContext
    private EntityManager entityManager;


    @Autowired
    public CCalendarLogic(PGCCalendarRepository pgCCalendarRepository) {
        this.pgCCalendarRepository = pgCCalendarRepository;
    }


    protected CCalendar create(CCalendar cCalendar) {
        return null;
    }

    protected List<CCalendar> getAll(Pageable pageable, Sort sort) {
        return null;
    }

    protected CCalendar getByUuid(UUID uuid) {
        return null;
    }

    protected CCalendar update(CCalendar cCalendar) {
        return null;
    }

    protected List<CCalendar> delete(List<UUID> cCalendars) {
        return null;
    }

}
