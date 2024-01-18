package com.acme.calendar.model.event;

import com.acme.calendar.model.common.CTimePeriod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CEventTest {

    private static final UUID TEST_UUID = UUID.randomUUID();
    private static final String TEST_TITLE = "TITLE";
    private static final String TEST_DESCRIPTION = "DESCRIPTION";
    private static final CTimePeriod TEST_TIMEPERIOD = null;
    private static final CRecurring TEST_RECURRING = null;


    private static Stream<CEvent> source() {
        return Stream.of(
                new CEvent(TEST_UUID, TEST_TITLE, TEST_DESCRIPTION, TEST_TIMEPERIOD, TEST_RECURRING)
        );
    }


    @ParameterizedTest
    @MethodSource("source")
    @DisplayName("Test ...")
    void testXXX(CEvent cEvent) {
        assertEquals(TEST_UUID, cEvent.uuid());
    }

    @Test
    @DisplayName("Test ...")
    void testXXXX() {
        CEvent cEvent = CEvent.builder()
                .title(TEST_TITLE)
                .build();

        assertEquals(TEST_TITLE, cEvent.title());
    }

}
