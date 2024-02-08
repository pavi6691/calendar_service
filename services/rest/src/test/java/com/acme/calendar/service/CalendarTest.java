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

import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CalendarTest extends AbstractBaseTest<Calendar> {
    PGCCalendarRepository pgcCalendarRepository;
    PGCEventRepository pgcEventRepository;
    
    @Autowired
    public CalendarTest(PGCCalendarRepository pgcCalendarRepository, PGCEventRepository pgcEventRepository) {
        super(pgcCalendarRepository);
        this.pgcCalendarRepository = pgcCalendarRepository;
        this.pgcEventRepository = pgcEventRepository;
    }
    
    @Test
    void test() {
        UUID calUUid = UUID.randomUUID();
        pgcCalendarRepository.save(Calendar.builder().uuid(calUUid).title("cal1").description("desc1").build());
        pgcEventRepository.save(Event.builder().uuid(UUID.randomUUID()).build());
    }
    
}
