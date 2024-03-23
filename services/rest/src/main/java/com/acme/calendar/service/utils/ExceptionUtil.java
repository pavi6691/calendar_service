package com.acme.calendar.service.utils;

import com.acme.calendar.core.enums.CalendarAPIError;
import com.acme.calendar.core.enums.KeyCloakAPIError;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class ExceptionUtil {


    public static void throwRestError(CalendarAPIError calendarAPIError, Object ...args) {
        throw new ResponseStatusException(HttpStatus.valueOf(calendarAPIError.httpStatusCode()), String.format(calendarAPIError.errorMessage(), args));
    }

    public static Exception newRestError(CalendarAPIError calendarAPIError, Object ...args) {
        return new ResponseStatusException(HttpStatus.valueOf(calendarAPIError.httpStatusCode()), String.format(calendarAPIError.errorMessage(), args));
    }

    public static void throwRestError(KeyCloakAPIError calendarAPIError, Object ...args) {
        throw new ResponseStatusException(HttpStatus.valueOf(calendarAPIError.httpStatusCode()), String.format(calendarAPIError.errorMessage(), args));
    }

}
