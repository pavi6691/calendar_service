package com.acme.calendar.service.controller;

public class ResponseModel {
    private String response;
    private int statusCode;

    public ResponseModel() {
    }

    public ResponseModel(String response, int statusCode) {
        this.response = response;
        this.statusCode = statusCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}