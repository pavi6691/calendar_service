package com.acme.calendar.service.service;

import com.acme.calendar.core.util.LogUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class AbstractService {

    @PersistenceContext
    public EntityManager entityManager;

    public int getOrder(UUID parentUuid) {
        log.debug("{}", LogUtil.method());
        if (parentUuid != null) {
            Query query = entityManager.createQuery("SELECT GREATEST(\n" +
                    "    (SELECT MAX(cal.childOrder) FROM CalendarMapping cal WHERE cal.parent.uuid = :parentId),\n" +
                    "    (SELECT MAX(co.childOrder) FROM CollectionMapping co WHERE co.parent.uuid = :parentId)\n" +
                    ")\n");
            query.setParameter("parentId", parentUuid);
            Integer maxChildOrder = (Integer) query.getSingleResult();
            return  (maxChildOrder != null) ? maxChildOrder + 1 : 0;
        }
        return 0;
    }
}
