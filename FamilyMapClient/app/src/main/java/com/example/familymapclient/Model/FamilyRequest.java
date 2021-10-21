package com.example.familymapclient.Model;

import Request.Request;

public class FamilyRequest extends Request {
    private String authToken;

    private Class type;

    public FamilyRequest(String authToken, Class type) {
        this.authToken = authToken;
        this.type = type;
    }

    public String getAuthToken() { return this.authToken; }

    public Class getType() { return this.type; }
}
