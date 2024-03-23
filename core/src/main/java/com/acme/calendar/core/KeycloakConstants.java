package com.acme.calendar.core;

public class KeycloakConstants {
    public static final String API_ENDPOINT_PREFIX = "/api/v1";

    public static final String API_ENDPOINT_KeyCloak = "/keycloak";

    public static final String SERVER_URL = "http://localhost:8080";
    public static final String REALM = "master";
    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "admin@1234";

    public static final String CLIENT_ID = "abhishek-khaiwale";
    public static final String CLIENT_SECRET = "72af9eb5-d558-42e6-86a3-30f94ac563c6";

    public static final String CREATE_USER = "/createUser";
    public static final String CREATE_GROUP = "/createGroup";
    public static final String CREATE_ROLE = "/createRole";
    public static final String ASSIGN_ROLE = "/inviteUser";
    public static final String ASSIGN_GROUP = "/assignGroup";
    public static final String ADD_USER_TO_GROUP = "/addUserToGroup";
    public static final String REVOKE_INVITE = "/roles/remove";
}
