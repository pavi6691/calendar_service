package com.acme.calendar.core.enums;


public enum CalendarAPIError {

    ERROR_CLIENT_REQUEST                   (400, 4000, "Client request error"),
    ERROR_PARSING_TIMESTAMP                (400, 4006, "Error parsing timestamp: %s"),
    ERROR_NOT_EXISTS_UUID                  (400, 4008, "No entry found for uuid: %s"),
    ERROR_ENTRY_HAS_NO_MODIFIED_DATE       (400, 4009, "Entry should have last modified date"),
    ERROR_NOT_FOUND                        (404, 4015, "No entries found"),
    ERROR_SOFT_DELETED                     (404, 4016, "Entry soft deleted"),
    ERROR_COLLECTION_NOT_FOUND             (404, 4017, "Collection not found. uuid: %s"),
    ERROR_CALENDAR_NOT_FOUND               (404, 4018, "Calendar not found. uuid: %s"),
    ERROR_ENTRY_HAS_BEEN_MODIFIED          (409, 4020, "Entry recently updated. Please reload. uuid: %s, updated on: %s"),
    INTERNAL_SERVER_ERROR_SERVER           (500, 5000, "%s"),
    ERROR_FETCHING_EVENTS                  (410,4021,"Error Fetching Events: %s");

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
