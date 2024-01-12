package com.acme.calendar.service.repository;

import com.acme.calendar.model.calendar.CCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface PGCCalendarRepository extends JpaRepository<CCalendar, UUID> {
}
