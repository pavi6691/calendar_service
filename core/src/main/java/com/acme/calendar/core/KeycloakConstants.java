package com.acme.calendar.core;

public class KeycloakConstants {
    public static final String API_ENDPOINT_PREFIX = "/api/v1";

    public static final String API_ENDPOINT_KeyCloak = "/auth";

    public static final String SERVER_URL = "http://88.99.248.68:8080";
    public static final String REALM = "master";
    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "admin@1234";

    public static final String CLIENT_ID = "72f2da9e-98fa-49cb-a51b-6ee372778852";
    public static final String CLIENT_NAME = "calendar-ui";
    public static final String CLIENT_SECRET = "DolbWZ231iBwF9G0KnDNGFQsXE0KYEnG";

    public static final String CREATE_USER = "/createUser";
    public static final String CREATE_GROUP = "/createGroup";
    public static final String CREATE_ROLE = "/createRole";
    public static final String ASSIGN_ROLE = "/assignRoleUser";
    public static final String INVITE_USER = "/inviteUser";
    public static final String ASSIGN_GROUP = "/assignRoleGroup";
    public static final String ADD_USER_TO_GROUP = "/addUserToGroup";
    public static final String REVOKE_INVITE = "/roles/remove";
    public static final String LOGIN =  "/login";
}
