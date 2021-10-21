package com.example.familymapclient.Model;

import Model.Event;

public class EventJsonObject extends Event {

    private boolean success;

    public EventJsonObject(String id, String user, String person_id, float latitude, float longitude,
                           String country, String city, String type, int year, boolean success) {
        super(id, user, person_id, latitude, longitude, country, city, type, year);
        this.success = success;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
