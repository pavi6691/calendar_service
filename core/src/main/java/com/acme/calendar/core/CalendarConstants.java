package com.acme.calendar.core;


public class CalendarConstants {


    /*
     * REST endpoints - query parameters
     */

    public static final String API_ENDPOINT_QUERY_PARAMETER_DELETE_PERMANENTLY = "deletePermanently";


    /*
     * REST endpoints - paths
     */

    public static final String API_ENDPOINT_PREFIX = "/api/v1";

    public static final String API_ENDPOINT_DEVOPS = "/devops";
    public static final String API_ENDPOINT_SETS = "/sets";
    public static final String API_ENDPOINT_CALENDARS = "/calendars";
    public static final String API_ENDPOINT_EVENTS = "/events";

    // /api/v1/devops/...
    public static final String API_ENDPOINT_DEVOPS_ERRORS = "/errors";

    // /api/v1/calendar...
    // POST
    public static final String API_ENDPOINT_SETS_CREATE = "";
    public static final String API_ENDPOINT_CALENDARS_CREATE = "";
    public static final String API_ENDPOINT_EVENTS_CREATE = "";
    // GET
    public static final String API_ENDPOINT_SETS_GET_ALL = "";
    public static final String API_ENDPOINT_CALENDARS_GET_ALL = "";
    public static final String API_ENDPOINT_EVENTS_GET_ALL = "";
    // PUT
    // ...
    // DELETE
    // ...

}
