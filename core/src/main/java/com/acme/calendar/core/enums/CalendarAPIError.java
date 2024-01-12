package com.acme.calendar.core.enums;


public enum CalendarAPIError {

    ERROR_CLIENT_REQUEST                   (400, 4000, "Client request error"),
    ERROR_PARSING_TIMESTAMP                (400, 4006, "Error parsing timestamp: %s"),
    ERROR_NOT_EXISTS_UUID                  (400, 4008, "No entry found for uuid: %s"),
    ERROR_NOT_FOUND                        (404, 4015, "No entries found"),
    ERROR_SOFT_DELETED                     (404, 4016, "Entry soft deleted"),
    ERROR_SERVER                           (500, 5000, "error message: %s");

    private final int httpStatusCode;
    private final int errorCode;
    private final String errorMessage;


    CalendarAPIError(int httpStatusCode, int errorCode, String errorMessage) {
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }


    public int httpStatusCode() {
        return this.httpStatusCode;
    }

    public int errorCode() {
        return this.errorCode;
    }

    public String errorMessage() {
        return this.errorMessage;
    }

}
