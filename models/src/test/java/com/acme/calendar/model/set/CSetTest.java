package com.acme.calendar.model.set;

import com.acme.calendar.model.calendar.CCalendar;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;


public class CSetTest {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);;

    private static final UUID TEST_UUID = UUID.randomUUID();
    private static final String TEST_TITLE = "TITLE";
    private static final String TEST_DESCRIPTION = "DESCRIPTION";

    private static final CSet TEST_SET_NO_ENTRIES = CSet.builder()
            .uuid(TEST_UUID)
            .title(TEST_TITLE)
            .description(TEST_DESCRIPTION)
            .build();
    private static final CCalendar TEST_CALENDAR = CCalendar.builder()
            .uuid(TEST_UUID)
            .title(TEST_TITLE)
            .description(TEST_DESCRIPTION)
            .build();
    private static final CSet TEST_CSET_WITH_SET_AND_CALENDAR = CSet.builder()
            .uuid(TEST_UUID)
            .title(TEST_TITLE)
            .description(TEST_DESCRIPTION)
            .entries(List.of(
                    CSetEntry.builder()
                            .set(TEST_SET_NO_ENTRIES)
                            .build(),
                    CSetEntry.builder()
                            .calendar(TEST_CALENDAR)
                            .build()
            ))
            .build();
    private static final CSet TEST_CSET_WITH_TWO_CALENDARS = CSet.builder()
            .uuid(TEST_UUID)
            .title(TEST_TITLE)
            .description(TEST_DESCRIPTION)
            .entries(List.of(
                    CSetEntry.builder()
                            .calendar(TEST_CALENDAR)
                            .build(),
                    CSetEntry.builder()
                            .calendar(TEST_CALENDAR)
                            .build()
            ))
            .build();


    @Test
    @DisplayName("Test ...")
    void testXXX() throws JsonProcessingException {
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(TEST_CSET_WITH_SET_AND_CALENDAR));
    }

    @Test
    @DisplayName("Test ...")
    void testXXXX() throws JsonProcessingException {
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(TEST_CSET_WITH_TWO_CALENDARS));
    }

}
