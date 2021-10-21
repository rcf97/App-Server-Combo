package com.example.familymapclient.Model;

import Model.Event;

public class EventsJsonObject {
    private Event[] data;
    private boolean success;
    private String message;

    public EventsJsonObject(Event[] data, boolean success) {
        this.data = data;
        this.success = success;

    }

    public EventsJsonObject(boolean success, String message) {
        this.message = message;
        this.success = success;

    }

    public Event[] getData() {
        return data;
    }

    public void setData(Event[] data) {
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
