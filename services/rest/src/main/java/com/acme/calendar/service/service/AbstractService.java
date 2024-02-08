package com.acme.calendar.service.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.UUID;

public class AbstractService {

    @PersistenceContext
    public EntityManager entityManager;

    public int getOrder(UUID parentUuid) {
        if (parentUuid != null) {
            Query query = entityManager.createQuery("SELECT GREATEST(\n" +
                    "    (SELECT MAX(cal.order) FROM Calendar cal WHERE cal.collection.uuid = :parentId),\n" +
                    "    (SELECT MAX(co.childOrder) FROM CollectionOrder co WHERE co.parent.uuid = :parentId)\n" +
                    ")\n");
            query.setParameter("parentId", parentUuid);
            Integer maxChildOrder = (Integer) query.getSingleResult();
            return  (maxChildOrder != null) ? maxChildOrder + 1 : 0;
        }
        return 0;
    }
}
