package com.acme.calendar.service.repository;

import com.acme.calendar.service.model.event.Event;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByStartTimeBetweenAndCalendarUuid(ZonedDateTime startDate, ZonedDateTime endDate, UUID calendarGuid);
}
