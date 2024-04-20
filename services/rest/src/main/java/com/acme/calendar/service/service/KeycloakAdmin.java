package com.acme.calendar.service.service;

import com.acme.calendar.core.KeycloakConstants;
import com.acme.calendar.core.enums.CalendarAPIError;
import com.acme.calendar.core.enums.KeyCloakAPIError;
import com.acme.calendar.service.model.User.UserCollectionCalendarMapping;
import jakarta.ws.rs.core.Response.Status;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;

import jakarta.ws.rs.core.Response;

import java.util.Collections;
import java.util.List;
import org.keycloak.representations.idm.authorization.GroupPolicyRepresentation;
import org.keycloak.representations.idm.authorization.GroupPolicyRepresentation.GroupDefinition;
import org.keycloak.representations.idm.authorization.PolicyRepresentation;
import org.keycloak.representations.idm.authorization.ResourcePermissionRepresentation;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;


import static com.acme.calendar.core.KeycloakConstants.*;
import static com.acme.calendar.core.KeycloakConstants.CLIENT_NAME;
import static com.acme.calendar.service.utils.ExceptionUtil.throwRestError;

@Slf4j

public class KeycloakAdmin {


    private static Keycloak keycloak;

    public KeycloakAdmin() {
        try{
            this.keycloak = KeycloakBuilder.builder()
                    .serverUrl(SERVER_URL)
                    .realm(REALM)
                    .grantType(OAuth2Constants.PASSWORD)
                    .username(ADMIN_USERNAME)
                    .password(ADMIN_PASSWORD)
                    .clientId(CLIENT_NAME)
                    .clientSecret(CLIENT_SECRET)
                    .build();
        }catch (Exception e){
            log.error(e.toString());
        }
    }

    public List<UserRepresentation> getUser(String userName){
        UsersResource usersResource = this.keycloak.realm(REALM).users();
        List<UserRepresentation> user = usersResource.search(userName, true);
        return user;
    }

    public Response createUser(String email, String firstName, String lastName, String password) {
        try {
            if (email == null || email.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Email cannot be null or empty").build();
            }
            RealmResource realmResource = this.keycloak.realm(REALM);
            UsersResource usersResource = realmResource.users();
            List<UserRepresentation> existingUsers = usersResource.search(email);
            boolean userExists = !existingUsers.isEmpty();
            UserRepresentation user = userExists ? existingUsers.get(0) : new UserRepresentation();
            user.setUsername(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setEnabled(true);

            if (password != null) {
                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setType(CredentialRepresentation.PASSWORD);
                credential.setValue(password);
                user.setCredentials(Collections.singletonList(credential));
            }
            if (userExists) {
                usersResource.get(user.getId()).update(user);
                return Response.status(Response.Status.OK).entity("User details updated successfully").build();
            } else {
                Response response = usersResource.create(user);
                if (response.getStatus() == 201) {
                    return Response.status(Response.Status.CREATED).entity("User created successfully").build();
                } else {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to create user").build();
                }
            }
        } catch (Exception e) {
            throwRestError(KeyCloakAPIError.ERROR_FAILED_TO_CREATE_USER, e.getMessage());
        }
        return null;
    }

    public Response createGroup(String groupName) {
        GroupRepresentation group = new GroupRepresentation();
        group.setName(groupName);
        log.debug("Groupname:"+ groupName);
        Response response = keycloak.realm(REALM).groups().add(group);
        if (response.getStatus() != 201) {
            log.error("Failed to create group: " + response.getStatusInfo());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to create group.").build();
        }
        return Response.status(Response.Status.CREATED).entity("Group Created Succesfully").build();
    }

    public Response createRole(String roleName) {
        try {
            RoleRepresentation role = new RoleRepresentation();
            role.setName(roleName);
            keycloak.realm(REALM).roles().create(role);
            return Response.status(Response.Status.CREATED).entity("Role Created successfully").build();
        }catch(Exception e){
            throwRestError(KeyCloakAPIError.ERROR_FAILED_TO_CREATE_ROLE, e.getMessage());
        }
        return null;
    }

    public Response assignRoleToUser(String username, String roleName) {
        try {
            if (!isValidRole(roleName.toLowerCase())) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid role. Role must be either admin, View, or Edit.").build();
            }
            UserRepresentation user = getUserByUsername(username);
            if (user == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("User with username " + username + " does not exist.").build();
            }
            RoleRepresentation role = keycloak.realm(REALM).roles().get(roleName).toRepresentation();
            keycloak.realm(REALM).users().get(user.getId()).roles().realmLevel().add(Collections.singletonList(role));
            return Response.status(Response.Status.CREATED).entity("Role " + roleName + " assigned successfully to user " + username).build();
        } catch (Exception e) {
            throwRestError(KeyCloakAPIError.ERROR_FAILED_TO_ASSIGN_ROLE, e.getMessage());
        }
        return null;
    }

    private static boolean isValidRole(String roleName) {
        List<RoleRepresentation> roles = keycloak.realm(REALM).roles().list();
        for (RoleRepresentation role : roles) {
            if (role.getName().equalsIgnoreCase(roleName)) {
                return true;
            }
        }
        return false;
    }

    private static UserRepresentation getUserByUsername(String username) {
        List<UserRepresentation> users = keycloak.realm(REALM).users().search(username);
        for (UserRepresentation user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public Response assignRoleToGroup(String roleName, String groupName) {
        try{
            GroupRepresentation group = keycloak.realm(REALM).groups().groups().stream()
                    .filter(g -> g.getName().equals(groupName))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Group not found: " + groupName));
            RoleRepresentation role = keycloak.realm(REALM).roles().get(roleName).toRepresentation();
            RoleMappingResource roleMappingResource = keycloak.realm(REALM).groups().group(group.getId()).roles();
            roleMappingResource.realmLevel().add(Collections.singletonList(role));
            return Response.status(Response.Status.CREATED).entity("Role "+roleName+" assigned successfully to group"+groupName).build();
        } catch (Exception e) {
            throwRestError(KeyCloakAPIError.ERROR_FAILED_TO_ASSIGN_ROLE, e.getMessage());
        }
        return null;
    }

    public Response addUserToGroup(String username, String groupName) {
        try {
            UsersResource usersResource = keycloak.realm(REALM).users();
            UserRepresentation userRepresentation = usersResource.search(username).get(0); // Assuming username is unique

            if (userRepresentation != null) {
                UserResource userResource = usersResource.get(userRepresentation.getId());
                GroupResource groupResource = keycloak.realm(REALM).groups().groups().stream()
                        .filter(group -> group.getName().equals(groupName))
                        .findFirst()
                        .map(groupRepresentation -> keycloak.realm(REALM).groups().group(groupRepresentation.getId()))
                        .orElse(null);
                if (groupResource != null) {
                    userResource.joinGroup(groupResource.toRepresentation().getId());
                    return Response.status(Response.Status.CREATED).entity("User added to group successfully").build();
                } else {
                    String errorMessage = "Group not found";
                    return Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
                }
            } else {
                String errorMessage = "User not found";
                return Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
            }
        } catch (Exception e) {
            throwRestError(KeyCloakAPIError.ERROR_FAILED_TO_ASSIGN_USER_TO_GROUP, e.getMessage());
        }
        return null;
    }

    public Response revokeRoleFromUser(String username, String roleName) {
        try {
            if (!isValidRole(roleName.toLowerCase())) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid role. Role must be either admin, View, or Edit.").build();
            }
            UserRepresentation user = getUserByUsername(username);
            if (user == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("User with username " + username + " does not exist.").build();
            }
            RoleRepresentation role = keycloak.realm(REALM).roles().get(roleName).toRepresentation();
            if (!userHasRole(user.getId(), role.getId())) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("User does not have the role " + roleName).build();
            }
            keycloak.realm(REALM).users().get(user.getId()).roles().realmLevel().remove(Collections.singletonList(role));
            return Response.status(Response.Status.OK).entity("Role " + roleName + " revoked successfully from user " + username).build();
        } catch (Exception e) {
            throwRestError(KeyCloakAPIError.ERROR_FAILED_TO_REVOKE_INVITE, e.getMessage());
        }
        return null;
    }

    public AccessTokenResponse authenticate(String email, String password) {

        String serverUrl = KeycloakConstants.SERVER_URL;
        String realm = KeycloakConstants.REALM;
        String clientSecret = KeycloakConstants.CLIENT_SECRET;

        Keycloak keycloak = KeycloakBuilder.builder()
            .serverUrl(serverUrl)
            .realm(realm)
            .username(email)
            .password(password)
            .clientId(CLIENT_NAME)
            .clientSecret(clientSecret)
            .build();

        AccessTokenResponse accessToken = keycloak.tokenManager().getAccessToken();
        return accessToken;
    }

    private static boolean userHasRole(String userId, String roleId) {
        return keycloak.realm(REALM).users().get(userId).roles().realmLevel().listEffective().stream()
            .anyMatch(role -> role.getId().equals(roleId));
    }

    public Response inviteUser(UserCollectionCalendarMapping userCollectionCalendarMapping) {
        try {
            RealmResource realmResource = this.keycloak.realm(REALM);
            UsersResource usersResource = realmResource.users();
            List<UserRepresentation> userDetals = usersResource.searchByUsername(userCollectionCalendarMapping.getUserName(), true);
            if (userDetals.size() == 0) {
                createUser(userCollectionCalendarMapping.getUserName(),null,null, null);
            }
            KeycloakAdmin keycloak = new KeycloakAdmin();
            String collectionGroupName = null;
            String calendarGroupName = null;

            if (userCollectionCalendarMapping.getCollection() != null) {
                for (String collectionUuid : userCollectionCalendarMapping.getCollection()) {
                    collectionGroupName = "collection_" + collectionUuid + "_" + userCollectionCalendarMapping.getUserName();
                    try {
                        System.out.println("Checking group: " + collectionGroupName);
                        keycloak.getGroupDetails(collectionGroupName);
                    } catch (GroupNotFoundException e) {
                        System.out.println("Group not found. Creating group: " + collectionGroupName);
                        keycloak.createGroup(collectionGroupName);
                    }
                    System.out.println("Adding user to group: " + userCollectionCalendarMapping.getUserName() + " -> " + collectionGroupName);
                    keycloak.addUserToGroup(userCollectionCalendarMapping.getUserName(), collectionGroupName);
                    System.out.println("Creating resource for group: " + collectionUuid + " -> " + collectionGroupName);
                    keycloak.createResource(collectionUuid, collectionGroupName);
                }
            }

            if (userCollectionCalendarMapping.getCalendar() != null) {
                for (String calendarUuid : userCollectionCalendarMapping.getCalendar()) {
                    calendarGroupName = "calendar_" + calendarUuid + "_" + userCollectionCalendarMapping.getUserName();
                    try {
                        System.out.println("Checking group: " + calendarGroupName);
                        keycloak.getGroupDetails(calendarGroupName);
                    } catch (GroupNotFoundException e) {
                        System.out.println("Group not found. Creating group: " + calendarGroupName);
                        keycloak.createGroup(calendarGroupName);
                    }
                    System.out.println("Adding user to group: " + userCollectionCalendarMapping.getUserName() + " -> " + calendarGroupName);
                    keycloak.addUserToGroup(userCollectionCalendarMapping.getUserName(), calendarGroupName);
                    System.out.println("Creating resource for group: " + calendarUuid + " -> " + calendarGroupName);
                    keycloak.createResource(calendarUuid, calendarGroupName);
                }
            }
            System.out.println("User " + userCollectionCalendarMapping.getUserName() + " invited Successfully");
            return Response.status(Status.ACCEPTED).entity("User " + userCollectionCalendarMapping.getUserName() + " invited Successfully ").build();
        } catch (Exception e) {
            throwRestError(CalendarAPIError.INTERNAL_SERVER_ERROR_SERVER, e.getMessage());
        }
        return null;
    }

    public boolean getGroupDetails(String groupName) throws GroupNotFoundException {
        RealmResource realmResource = keycloak.realm(REALM);
        GroupRepresentation groupRepresentation = realmResource.groups().groups().stream()
            .filter(group -> group.getName().equals(groupName))
            .findFirst()
            .orElseThrow(() -> new GroupNotFoundException("Group not found: " + groupName));
        return false;
    }

    public void createResource(String resourceName, String group ) {
        try {
            RealmResource realmResource = keycloak.realm(REALM);
            ResourcesResource resourcesResource = realmResource.clients().get(CLIENT_ID).authorization().resources();
            Random random = new Random();

            // Check if resource already exists
            String permissionName = group + "_" + random.nextInt(10000);
            String policyName = group + "_" + random.nextInt(10000);
            try {
                ResourceRepresentation existingResource = resourcesResource.findByName(resourceName).getFirst();
                if (existingResource != null) {
                    attachPermissionToResource(resourceName, permissionName, policyName, group);
                    throwRestError(KeyCloakAPIError.ERROR_RESOURCE_ALREADY_EXISTS, resourceName);
                }
            }catch(Exception e){
                System.out.println("Error"+e);
            }
            ResourceRepresentation resourceRepresentation = new ResourceRepresentation();
            resourceRepresentation.setName(resourceName);
            Response response = resourcesResource.create(resourceRepresentation);

            // Check the response status
            if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                System.out.println("Resource '" + resourceName + "' created successfully.");
            } else {
                System.out.println("Failed to create resource '" + resourceName + "'. Status code: " + response.getStatus());
            }
            attachPermissionToResource(resourceName, permissionName, policyName, group);
        } catch (Exception e) {
            System.err.println("An error occurred while creating the resource: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void attachPermissionToResource(String resourceName, String permissionName, String policyName, String group ) {
        try{

            RealmResource realmResource = keycloak.realm(REALM);
            ClientResource clientResource = realmResource.clients().get(CLIENT_ID);
            ResourcesResource resourcesResource = realmResource.clients().get(CLIENT_ID).authorization().resources();

            List<ResourceRepresentation> resources = resourcesResource.findByName(resourceName);
            if (resources.isEmpty()) {
                System.err.println("Resource '" + resourceName + "' not found.");
                return;
            }
            String resourceId = resources.get(0).getId();
            ResourcePermissionsResource resourcePermissionsResource = clientResource.authorization().permissions().resource();
            ResourcePermissionRepresentation permissionRepresentation = new ResourcePermissionRepresentation();
            permissionRepresentation.setName(permissionName);

            createGroupPolicy(policyName, group);
            permissionRepresentation.addPolicy(policyName);

            permissionRepresentation.addResource(resourceId);
            Response response = resourcePermissionsResource.create(permissionRepresentation);

            System.out.println("Response"+response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createGroupPolicy(String policyName, String groupName){
        RealmResource realmResource = keycloak.realm(REALM);

        GroupsResource groupsResource = realmResource.groups(); // Replace with your client ID

        // Check if the group exists
        List<String> groupIds = groupsResource.groups().stream()
            .filter(group -> group.getName().equals(groupName))
            .map(group -> group.getId())
            .collect(Collectors.toList());

        if (groupIds.isEmpty()) {
            throwRestError(KeyCloakAPIError.ERROR_GROUP_NAME_NOT_FOUND, groupName);
        }
        GroupPolicyRepresentation groupPolicy = new GroupPolicyRepresentation();
        groupPolicy.setName(policyName);

        Set<GroupDefinition> groupDefinitions = new HashSet<>();
        GroupDefinition groupDefinition = new GroupDefinition();
        groupDefinition.setId(groupIds.get(0));
        groupDefinition.setExtendChildren(false); // Or any other desired setting
        groupDefinitions.add(groupDefinition);
        groupPolicy.setGroups(groupDefinitions);

        GroupPoliciesResource policyResource = realmResource.clients().get(CLIENT_ID).authorization().policies().group(); // Replace with your client ID
        Response response = policyResource.create(groupPolicy);

        if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
            System.out.println("Group-based policy '" + policyName + "' created successfully.");
        } else {
            System.out.println("Failed to create group-based policy. Status code: " + response.getStatus());
        }
    }

    public Set<GroupDefinition> getAssociatedGroup(String resourceName){
        RealmResource realmResource = keycloak.realm(REALM);
        ClientResource clientResource = realmResource.clients().get(CLIENT_ID);
        ResourcesResource resourcesResource = realmResource.clients().get(CLIENT_ID).authorization().resources();
        List<ResourceRepresentation> resources = resourcesResource.findByName(resourceName);
        if (resources.isEmpty()) {
            return Collections.emptySet();
        }
        String resourceId = resources.get(0).getId();
        try {
            Set<GroupDefinition> groupset = new HashSet<>();
            List<PolicyRepresentation> pList = clientResource.authorization().resources().resource(resourceId).permissions();
            for( int i=0; i<pList.size(); i++ ){
                ResourcePermissionResource permissionResource = clientResource.authorization().permissions().resource().findById(pList.get(i).getId());
                List<PolicyRepresentation> reslist = permissionResource.associatedPolicies();
                for(int j=0; j<reslist.size();j++){
                    GroupPolicyRepresentation grp = clientResource.authorization().policies().group().findById(reslist.get(j).getId()).toRepresentation();
                    groupset.addAll(grp.getGroups());
                }
            }
            return groupset;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return Collections.emptySet();

        }
    }

    public boolean isUserInGroup(Authentication authentication, String groupId) {
        String userId = extractPreferredUsername(authentication);
        RealmResource realmResource = keycloak.realm(REALM);
        UsersResource usersResource = realmResource.users();
        List<UserRepresentation> userResource = usersResource.search(userId);
        List<GroupRepresentation> groups = realmResource.users().get(userResource.get(0).getId()).groups();
        for (GroupRepresentation group : groups) {
            if (group.getId().equals(groupId)) {
                return true;
            }
        }
        return false;
    }

    private String extractPreferredUsername(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            Map<String, Object> claims = jwt.getClaims();
            return (String) claims.get("preferred_username");
        }
        return null;
    }


}
