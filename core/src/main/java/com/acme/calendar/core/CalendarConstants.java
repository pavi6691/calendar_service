package com.acme.calendar.core;


public class CalendarConstants {


    /*
     * REST endpoints - query parameters
     */

    public static final String API_QUERY_DELETE_PERMANENTLY = "deletePermanently";


    /*
     * REST endpoints - path parameters
     */

    public static final String API_PATH_COLLECTION_UUID = "uuid";
    public static final String API_PATH_CALENDAR_UUID = "uuid";
    public static final String API_PATH_EVENT_UUID = "uuid";

    /*
     * REST endpoints - paths
     */

    public static final String API_ENDPOINT_PREFIX = "/api/v1";

    public static final String API_ENDPOINT_DEVOPS = "/devops";

    public static final String API_ENDPOINT_COLLECTIONS = "/collections";
    public static final String API_ENDPOINT_CALENDARS = "/calendars";
    public static final String API_ENDPOINT_EVENTS = "/events";

    // /api/v1/devops/...
    public static final String API_ENDPOINT_DEVOPS_ERRORS = "/errors";

    // /api/v1/calendar...
    // POST
    public static final String API_ENDPOINT_COLLECTIONS_CREATE = "";
    public static final String API_ENDPOINT_CALENDARS_CREATE = "";
    public static final String API_ENDPOINT_EVENTS_CREATE = "";
    // GET
    public static final String API_ENDPOINT_COLLECTIONS_GET_ALL = "";
    public static final String API_ENDPOINT_COLLECTIONS_GET_BY_UUID = "/{" + API_PATH_COLLECTION_UUID + "}";
    public static final String API_ENDPOINT_CALENDARS_GET_ALL = "";
    public static final String API_ENDPOINT_CALENDARS_GET_BY_UUID = "/{" + API_PATH_CALENDAR_UUID + "}";
    public static final String API_ENDPOINT_EVENTS_GET_ALL = "";
    public static final String API_ENDPOINT_EVENTS_GET_BY_UUID = "/{" + API_PATH_EVENT_UUID + "}";
    // PUT
    public static final String API_ENDPOINT_COLLECTIONS_UPDATE = "";
    public static final String API_ENDPOINT_CALENDARS_UPDATE = "";
    public static final String API_ENDPOINT_EVENTS_UPDATE = "";
    // DELETE
    public static final String API_ENDPOINT_COLLECTIONS_DELETE = "";
    public static final String API_ENDPOINT_CALENDARS_DELETE = "";
    public static final String API_ENDPOINT_EVENTS_DELETE = "";

}
