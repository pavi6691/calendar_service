package com.acme.calendar.service;

import com.acme.calendar.service.base.AbstractBaseTest;
import com.acme.calendar.service.model.calendar.Calendar;
import com.acme.calendar.service.model.event.Event;
import com.acme.calendar.service.repository.PGCCalendarRepository;
import com.acme.calendar.service.repository.PGCEventRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZonedDateTime;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CalendarTest extends AbstractBaseTest<Calendar> {
    PGCCalendarRepository pgcCalendarRepository;
    PGCEventRepository pgcEventRepository;

    private static final UUID TEST_CALENDAR_UUID = UUID.randomUUID();
    private static final String TEST_CALENDAR_TITLE = "Calendar title";
    private static final String TEST_CALENDAR_DESCRIPTION = "Calendar description";

    private static final UUID TEST_EVENT_UUID = UUID.randomUUID();
    private static final String TEST_EVENT_TITLE = "Event title";
    private static final String TEST_EVENT_DESCRIPTION = "Event description";
    private static final ZonedDateTime TEST_EVENT_STARTTIME = ZonedDateTime.now();
    private static final ZonedDateTime TEST_EVENT_ENDTIME = ZonedDateTime.now();

    private static final Calendar TEST_CALENDAR = Calendar.builder()
            .uuid(TEST_CALENDAR_UUID)
            .title(TEST_CALENDAR_TITLE)
            .description(TEST_CALENDAR_DESCRIPTION)
            .build();
    private static final Event TEST_EVENT = Event.builder()
            .uuid(TEST_EVENT_UUID)
            .title(TEST_EVENT_TITLE)
            .description(TEST_EVENT_DESCRIPTION)
            .startTime(TEST_EVENT_STARTTIME)
            .endTime(TEST_EVENT_ENDTIME)
            .calendar(TEST_CALENDAR)
            .build();

    
    @Autowired
    public CalendarTest(PGCCalendarRepository pgcCalendarRepository, PGCEventRepository pgcEventRepository) {
        super(pgcCalendarRepository);
        this.pgcCalendarRepository = pgcCalendarRepository;
        this.pgcEventRepository = pgcEventRepository;
    }
    
    @Test
    void test() {
        pgcCalendarRepository.save(TEST_CALENDAR);
        pgcEventRepository.save(TEST_EVENT);
    }
    
}
