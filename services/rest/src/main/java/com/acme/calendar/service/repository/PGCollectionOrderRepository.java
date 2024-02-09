package com.acme.calendar.service.repository;

import com.acme.calendar.service.model.collections.CollectionOrder;
import com.acme.calendar.service.model.collections.CollectionOrderPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PGCollectionOrderRepository extends JpaRepository<CollectionOrder, CollectionOrderPK> {

}
