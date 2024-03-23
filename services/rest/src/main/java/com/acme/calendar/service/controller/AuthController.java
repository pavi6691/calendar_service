package com.acme.calendar.service.controller;

import com.acme.calendar.core.KeycloakConstants;
import com.acme.calendar.service.model.User.UserRequest;
import com.acme.calendar.service.service.KeycloakAdmin;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = KeycloakConstants.API_ENDPOINT_PREFIX + KeycloakConstants.API_ENDPOINT_KeyCloak)
public class AuthController {

    private final KeycloakAdmin keycloak;

    @Autowired
    public AuthController() {
        this.keycloak = new KeycloakAdmin();
    }

    @PostMapping(path=KeycloakConstants.CREATE_USER,consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response createUser(@RequestBody UserRequest userRequest) {
        return keycloak.createUser(userRequest.username, userRequest.firstName, userRequest.lastName, userRequest.email);
    }

    @PostMapping(path=KeycloakConstants.CREATE_GROUP,consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response createGroup(@RequestBody UserRequest userRequest) {
        return keycloak.createGroup(userRequest.group);
    }

    @PostMapping(path=KeycloakConstants.CREATE_ROLE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response createRole(@RequestBody UserRequest userRequest) {
        return keycloak.createRole(userRequest.role);
    }

    @PostMapping(path=KeycloakConstants.ASSIGN_ROLE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response assignRole(@RequestBody UserRequest userRequest) {
        return keycloak.assignRoleToUser(userRequest.username,userRequest.role);
    }

    @PostMapping(path=KeycloakConstants.ASSIGN_GROUP,consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response assignGroup(@RequestBody UserRequest userRequest) {
        return keycloak.assignRoleToGroup(userRequest.role,userRequest.group);
    }

    @PostMapping(path=KeycloakConstants.ADD_USER_TO_GROUP,consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response addUserToGroup(@RequestBody UserRequest userRequest) {
        return keycloak.addUserToGroup(userRequest.username,userRequest.group);
    }

    @DeleteMapping(path = KeycloakConstants.REVOKE_INVITE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response removeUserRole(@RequestBody UserRequest userRoleRequest) {
        return keycloak.revokeRoleFromUser(userRoleRequest.username, userRoleRequest.role);
    }
}
