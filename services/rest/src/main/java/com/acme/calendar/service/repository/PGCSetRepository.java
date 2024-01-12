package com.acme.calendar.service.repository;

import com.acme.calendar.model.set.CSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface PGCSetRepository extends JpaRepository<CSet, UUID> {
}
