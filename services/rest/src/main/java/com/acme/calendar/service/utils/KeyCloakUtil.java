package com.acme.calendar.service.utils;


import com.acme.calendar.service.controller.ResponseModel;
import jakarta.ws.rs.core.Response;

public class KeyCloakUtil {
    public static ResponseModel extractUserData(Response response) {

        Object entity = response.getEntity();
        int statusCode = response.getStatus();

        ResponseModel responseModel = new ResponseModel();
        String responseString = entity.toString();

        responseModel.setResponse(responseString);
        responseModel.setStatusCode(statusCode);

        return responseModel;
    }
}