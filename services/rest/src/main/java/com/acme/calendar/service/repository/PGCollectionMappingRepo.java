package com.acme.calendar.service.repository;

import com.acme.calendar.service.model.collections.CollectionMapping;
import com.acme.calendar.service.model.collections.MappingPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PGCollectionMappingRepo extends JpaRepository<CollectionMapping,MappingPK> {
}
