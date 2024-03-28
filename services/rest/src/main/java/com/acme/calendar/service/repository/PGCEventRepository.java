package com.acme.calendar.service.repository;

import com.acme.calendar.service.model.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;


@Repository
public interface PGCEventRepository extends JpaRepository<Event, UUID> {
    List<Event> findByStartTimeBetweenAndCalendarUuid(ZonedDateTime startDate, ZonedDateTime endDate, UUID calendarGuid);
    List<Event> findByCalendarUuid(UUID calendarGuid);
}
