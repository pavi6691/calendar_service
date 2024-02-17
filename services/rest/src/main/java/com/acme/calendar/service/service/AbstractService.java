package com.acme.calendar.service.service;

import com.acme.calendar.core.util.LogUtil;
import com.acme.calendar.service.model.calendar.Calendar;
import com.acme.calendar.service.model.collections.Collection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
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

    protected void build(Calendar calendar) {
        if(calendar.getUuid() != null) {
            calendar.setUuid(calendar.getUuid());
        } else {
            calendar.setUuid(UUID.randomUUID());
        }
        if(calendar.getCreatedInitially() == null) {
            ZonedDateTime zonedDateTime = ZonedDateTime.now();
            calendar.setCreatedInitially(zonedDateTime);
            calendar.setLastUpdatedTime(zonedDateTime);
        }
    }

    protected void build(Collection collection) {
        if(collection.getUuid() != null) {
            collection.setUuid(collection.getUuid());
        } else {
            collection.setUuid(UUID.randomUUID());
        }
        if(collection.getCreatedInitially() == null) {
            ZonedDateTime zonedDateTime = ZonedDateTime.now();
            collection.setCreatedInitially(zonedDateTime);
            collection.setLastUpdatedTime(zonedDateTime);
        }
    }
}
