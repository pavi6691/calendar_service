package com.acme.calendar.service.controller;

import com.acme.calendar.core.KeycloakConstants;
import com.acme.calendar.core.enums.KeyCloakAPIError;
import com.acme.calendar.service.model.User.GroupRequest;
import com.acme.calendar.service.model.User.JwtResponse;
import com.acme.calendar.service.model.User.LoginRequest;
import com.acme.calendar.service.model.User.RoleGroupRequest;
import com.acme.calendar.service.model.User.RoleRequest;
import com.acme.calendar.service.model.User.UserCollectionCalendarMapping;
import com.acme.calendar.service.model.User.UserDataSet;
import com.acme.calendar.service.model.User.UserRequest;
import com.acme.calendar.service.model.User.UserRoleRequest;
import com.acme.calendar.service.model.User.UserToGroupRequest;
import com.acme.calendar.service.service.KeycloakAdmin;
import com.acme.calendar.service.utils.KeyCloakUtil;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
        Response response = keycloak.createUser(userRequest.email, userRequest.firstName, userRequest.lastName, userRequest.password);
        ResponseModel responseModel = KeyCloakUtil.extractUserData(response);
        return ResponseEntity.ok(responseModel);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping(path=KeycloakConstants.CREATE_GROUP,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel> createGroup(@RequestBody GroupRequest groupRequest) {
        Response response = keycloak.createGroup(groupRequest.group);
        ResponseModel responseModel = KeyCloakUtil.extractUserData(response);
        return ResponseEntity.ok(responseModel);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping(path=KeycloakConstants.CREATE_ROLE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel> createRole(@RequestBody RoleRequest roleRequest) {
        Response response =  keycloak.createRole(roleRequest.role);
        ResponseModel responseModel = KeyCloakUtil.extractUserData(response);
        return ResponseEntity.ok(responseModel);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping(path=KeycloakConstants.ASSIGN_ROLE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel> assignRole(@RequestBody UserRoleRequest userRoleRequest) {
        Response response = keycloak.assignRoleToUser(userRoleRequest.username,userRoleRequest.role);
        ResponseModel responseModel = KeyCloakUtil.extractUserData(response);
        return ResponseEntity.ok(responseModel);
    }

    @PostMapping(path = KeycloakConstants.INVITE_USER, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel> inviteUser(@RequestBody UserDataSet userDataSet) {
        try {
            List<UserCollectionCalendarMapping> userMappings = userDataSet.getItems();
            for (UserCollectionCalendarMapping userMapping : userMappings) {
                keycloak.inviteUser(userMapping);
            }
            return ResponseEntity.ok(new ResponseModel("Users invited successfully", HttpStatus.OK.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseModel("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping(path=KeycloakConstants.ASSIGN_GROUP,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel> assignGroup(@RequestBody RoleGroupRequest roleGroupRequest) {
        Response response = keycloak.assignRoleToGroup(roleGroupRequest.role,roleGroupRequest.group);
        ResponseModel responseModel = KeyCloakUtil.extractUserData(response);
        return ResponseEntity.ok(responseModel);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping(path=KeycloakConstants.ADD_USER_TO_GROUP,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel> addUserToGroup(@RequestBody UserToGroupRequest userToGroupRequest) {
        Response response = keycloak.addUserToGroup(userToGroupRequest.username, userToGroupRequest.group);
        ResponseModel responseModel = KeyCloakUtil.extractUserData(response);
        return ResponseEntity.ok(responseModel);
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping(path = KeycloakConstants.REVOKE_INVITE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseModel> removeUserRole(@RequestBody UserRoleRequest userRoleRequest) {
        Response response = keycloak.revokeRoleFromUser(userRoleRequest.username, userRoleRequest.role);
        ResponseModel responseModel = KeyCloakUtil.extractUserData(response);
        return ResponseEntity.ok(responseModel);
    }

    @PostMapping(path = KeycloakConstants.LOGIN)
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        AccessTokenResponse token = null;
        try {
            token = keycloak.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        } catch (Exception e) {
            return ResponseEntity.status(KeyCloakAPIError.ERROR_LOGIN_FAILED_CHECK_USERNAME_PASSWORD.httpStatusCode()).body(KeyCloakAPIError.ERROR_LOGIN_FAILED_CHECK_USERNAME_PASSWORD.errorMessage());
        }
        return ResponseEntity.ok(new JwtResponse(token.getToken()));
    }
}
