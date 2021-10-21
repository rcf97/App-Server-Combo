package com.example.familymapclient.Model;

import Model.Person;

public class PersonJsonObject extends Person {
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public PersonJsonObject(String id, String username, String firstname, String lastname,
                            String gender, String fatherid, String motherid, String spouseid) {
        super(id, username, firstname, lastname, gender, fatherid, motherid, spouseid);
    }

    public PersonJsonObject(String username, String firstname, String lastname, String gender) {
        super(username, firstname, lastname, gender);
    }

    public PersonJsonObject(String id, String username, String firstname, String lastname,
                            String gender, String fatherid, String motherid, String spouseid,
                            boolean success) {
        super(id, username, firstname, lastname, gender, fatherid, motherid, spouseid);
        this.success = success;
    }

}
