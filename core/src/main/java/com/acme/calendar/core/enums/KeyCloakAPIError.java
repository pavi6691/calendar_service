package com.acme.calendar.core.enums;

public enum KeyCloakAPIError {

    ERROR_FAILED_TO_ASSIGN_ROLE            (425, 4025, "Failed to Assign role to group: %s"),
    ERROR_FAILED_TO_CREATE_USER            (426, 4026, "Failed to create user: %s"),
    ERROR_FAILED_TO_CREATE_ROLE            (427, 4027, "Failed to create role: %s"),
    ERROR_FAILED_TO_ASSIGN_USER_TO_GROUP      (428, 4028, "Failed to create role: %s"),
    ERROR_FAILED_TO_REVOKE_INVITE      (429, 4029, "Failed to revoke invite: %s"),
    ERROR_LOGIN_FAILED_CHECK_USERNAME_PASSWORD (433, 4033, "Login failed: Invalid username or password");

    private final int httpStatusCode;
    private final int errorCode;
    private final String errorMessage;


    KeyCloakAPIError(int httpStatusCode, int errorCode, String errorMessage) {
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
