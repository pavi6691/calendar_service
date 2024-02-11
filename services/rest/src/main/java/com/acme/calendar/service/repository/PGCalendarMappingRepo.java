package com.acme.calendar.service.repository;

import com.acme.calendar.service.model.calendar.CalendarMapping;
import com.acme.calendar.service.model.collections.CollectionOrderPK;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PGCalendarMappingRepo extends JpaRepository<CalendarMapping, CollectionOrderPK> {
    
}
