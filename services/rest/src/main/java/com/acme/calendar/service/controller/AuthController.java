package com.acme.calendar.service.controller;

import com.acme.calendar.core.KeycloakConstants;
import com.acme.calendar.service.model.User.GroupRequest;
import com.acme.calendar.service.model.User.RoleGroupRequest;
import com.acme.calendar.service.model.User.RoleRequest;
import com.acme.calendar.service.model.User.UserRequest;
import com.acme.calendar.service.model.User.UserRoleRequest;
import com.acme.calendar.service.model.User.UserToGroupRequest;
import com.acme.calendar.service.service.KeycloakAdmin;
import com.acme.calendar.service.utils.KeyCloakUtil;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseModel> createUser(@RequestBody UserRequest userRequest) {
        Response response = keycloak.createUser(userRequest.username, userRequest.firstName, userRequest.lastName, userRequest.email);
        ResponseModel responseModel = KeyCloakUtil.extractUserData(response);
        return ResponseEntity.ok(responseModel);
    }

    @PostMapping(path=KeycloakConstants.CREATE_GROUP,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel> createGroup(@RequestBody GroupRequest groupRequest) {
        Response response = keycloak.createGroup(groupRequest.group);
        ResponseModel responseModel = KeyCloakUtil.extractUserData(response);
        return ResponseEntity.ok(responseModel);
    }

    @PostMapping(path=KeycloakConstants.CREATE_ROLE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel> createRole(@RequestBody RoleRequest roleRequest) {
        Response response =  keycloak.createRole(roleRequest.role);
        ResponseModel responseModel = KeyCloakUtil.extractUserData(response);
        return ResponseEntity.ok(responseModel);
    }

    @PostMapping(path=KeycloakConstants.ASSIGN_ROLE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel> assignRole(@RequestBody UserRoleRequest userRoleRequest) {
        Response response = keycloak.assignRoleToUser(userRoleRequest.username,userRoleRequest.role);
        ResponseModel responseModel = KeyCloakUtil.extractUserData(response);
        return ResponseEntity.ok(responseModel);
    }

    @PostMapping(path=KeycloakConstants.ASSIGN_GROUP,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel> assignGroup(@RequestBody RoleGroupRequest roleGroupRequest) {
        Response response = keycloak.assignRoleToGroup(roleGroupRequest.role,roleGroupRequest.group);
        ResponseModel responseModel = KeyCloakUtil.extractUserData(response);
        return ResponseEntity.ok(responseModel);
    }

    @PostMapping(path=KeycloakConstants.ADD_USER_TO_GROUP,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel> addUserToGroup(@RequestBody UserToGroupRequest userToGroupRequest) {
        Response response = keycloak.addUserToGroup(userToGroupRequest.username, userToGroupRequest.group);
        ResponseModel responseModel = KeyCloakUtil.extractUserData(response);
        return ResponseEntity.ok(responseModel);
    }

    @DeleteMapping(path = KeycloakConstants.REVOKE_INVITE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel> removeUserRole(@RequestBody UserRoleRequest userRoleRequest) {
        Response response = keycloak.revokeRoleFromUser(userRoleRequest.username, userRoleRequest.role);
        ResponseModel responseModel = KeyCloakUtil.extractUserData(response);
        return ResponseEntity.ok(responseModel);
    }
}
