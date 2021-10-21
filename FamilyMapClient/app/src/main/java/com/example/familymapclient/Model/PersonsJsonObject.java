package com.example.familymapclient.Model;

import Model.Person;

public class PersonsJsonObject {
    private Person[] data;
    private boolean success;
    private String message;

    public PersonsJsonObject(Person[] data, boolean success) {
        this.data = data;
        this.success = success;
    }

    public PersonsJsonObject(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Person[] getData() {
        return data;
    }

    public void setData(Person[] data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() { return this.message; }
}
