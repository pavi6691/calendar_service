package com.acme.calendar.core.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests for {@link CalendarAPIError} enums
 */
public class CalendarAPIErrorTest {

    private final static int[] VALID_HTTP_STATUS_CODES = { 400, 404, 408, 409, 500 };


    @Test
    @DisplayName("Test all enums")
    void testAllEnums() {
        Arrays.stream(CalendarAPIError.values()).forEach(calendarAPIError -> {
            String result = switch (calendarAPIError) {
                case ERROR_CLIENT_REQUEST,
                        ERROR_PARSING_TIMESTAMP,
                        ERROR_NOT_EXISTS_UUID,
                        ERROR_NOT_FOUND,
                        ERROR_SERVER,
                        ERROR_SOFT_DELETED -> {
                    String s = String.format("Checking: %s (%s, %s, \"%s\")", calendarAPIError.name(), calendarAPIError.httpStatusCode(), calendarAPIError.errorCode(), calendarAPIError.errorMessage());
                    System.out.println(s);
                    assertTrue(Arrays.stream(VALID_HTTP_STATUS_CODES).anyMatch(i -> i == calendarAPIError.httpStatusCode()), () -> String.format("httpStatusCode: %s is not between valid values", calendarAPIError.httpStatusCode()));
                    assertTrue(calendarAPIError.errorCode() >= 4000 && calendarAPIError.errorCode() < 6000, () -> String.format("errorCode: %s is not between valid values", calendarAPIError.errorCode()));
                    assertNotNull(calendarAPIError.errorMessage(), () -> "Error message can not be null");
                    yield s;
                }
            };
        });
    }

    @Test
    @DisplayName("Check that no two errors has the same errorCode")
    void testUniqueErrorCodes() {
        List<Integer> list = new ArrayList<>();
        Arrays.stream(CalendarAPIError.values()).forEach(calendarAPIError -> {
            int errorCode = calendarAPIError.errorCode();
            assertFalse(list.contains(errorCode), () -> String.format("Seems that errorCodes are not unique: %s", errorCode));
            list.add(errorCode);
        });
        System.out.println("Found the following errorCodes (all unique): " + list.stream().sorted().map(Object::toString).collect(Collectors.joining(", ")));
    }

}
