package com.acme.calendar.core;

public class KeycloakConstants {
    public static final String API_ENDPOINT_PREFIX = "/api/v1";

    public static final String API_ENDPOINT_KeyCloak = "/auth";

    public static final String SERVER_URL = "http://localhost:8080";
    public static final String REALM = "master";
    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "admin@1234";

    public static final String CLIENT_ID = "0c6da51a-08cc-45d3-8a44-f1c930aebad7";
    public static final String CLIENT_NAME = "abhishek-khaiwale";
    public static final String CLIENT_SECRET = "dSpdPWkChJ9MM3qTCCw9b2rz9OmTPt8O";

    public static final String CREATE_USER = "/createUser";
    public static final String CREATE_GROUP = "/createGroup";
    public static final String CREATE_ROLE = "/createRole";
    public static final String ASSIGN_ROLE = "/assignRole";
    public static final String INVITE_USER = "/inviteUser";
    public static final String ASSIGN_GROUP = "/assignGroup";
    public static final String ADD_USER_TO_GROUP = "/addUserToGroup";
    public static final String REVOKE_INVITE = "/roles/remove";
    public static final String LOGIN =  "/login";
}
