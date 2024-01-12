package com.acme.calendar.service.logic;

import com.acme.calendar.model.event.CEvent;
import com.acme.calendar.service.repository.PGCEventRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class CEventLogic {

    PGCEventRepository pgCEventRepository;
    @PersistenceContext
    private EntityManager entityManager;


    @Autowired
    public CEventLogic(PGCEventRepository pgCEventRepository) {
        this.pgCEventRepository = pgCEventRepository;
    }


    protected CEvent create(CEvent cEvent) {
        return null;
    }

    protected List<CEvent> getAll(Pageable pageable, Sort sort) {
        return null;
    }

    protected CEvent getByUuid(UUID uuid) {
        return null;
    }

    protected CEvent update(CEvent cEvent) {
        return null;
    }

    protected List<CEvent> delete(List<UUID> cEvents) {
        return null;
    }

}
