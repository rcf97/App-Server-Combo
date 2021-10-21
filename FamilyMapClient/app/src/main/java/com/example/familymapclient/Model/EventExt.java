package com.example.familymapclient.Model;

import Model.Event;

public class EventExt extends Event {

    private PersonExt person;

    public EventExt(Event event) {
        super(event.getID(), event.getUser(), event.getPerson_ID(), event.getLatitude(), event.getLongitude(),
                event.getCountry(), event.getCity(), event.getType(), event.getYear());

    }

    public boolean search(String searchStr) {
        if (this.getCountry().toLowerCase().contains(searchStr.toLowerCase()) ||
                this.getCity().toLowerCase().contains(searchStr.toLowerCase()) ||
                this.getType().toLowerCase().contains(searchStr.toLowerCase()) ||
                ("" + this.getYear()).toLowerCase().contains(searchStr.toLowerCase())) {
            return true;
        } else {
            return false;
        }
    }

    public void setPerson(PersonExt person) {
        this.person = person;
    }

    public PersonExt getPerson() {
        return this.person;
    }
}
