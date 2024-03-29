package com.acme.calendar.service.repository;

import com.acme.calendar.service.model.collections.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface PGCollectionsRepository extends JpaRepository<Collection, UUID> {
}
