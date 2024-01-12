package com.acme.calendar.service.logic;

import com.acme.calendar.model.set.CSet;
import com.acme.calendar.service.repository.PGCSetRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class CSetLogic {

    PGCSetRepository pgCSetRepository;
    @PersistenceContext
    private EntityManager entityManager;


    @Autowired
    public CSetLogic(PGCSetRepository pgCSetRepository) {
        this.pgCSetRepository = pgCSetRepository;
    }


    protected CSet create(CSet cSet) {
        return null;
    }

    protected List<CSet> getAll(Pageable pageable, Sort sort) {
        return null;
    }

    protected CSet getByUuid(UUID uuid) {
        return null;
    }

    protected CSet update(CSet cSet) {
        return null;
    }

    protected List<CSet> delete(List<UUID> cSets) {
        return null;
    }

}
