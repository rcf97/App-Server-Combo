package com.example.familymapclient.Model;

import java.util.ArrayList;

import Model.Person;

public class PersonExt extends Person {

    private ArrayList<EventExt> events;

    private PersonExt mother;

    private PersonExt father;

    private PersonExt spouse;

    private PersonExt child;

    public PersonExt(Person person) {
        super(person.getID(), person.getUsername(), person.getFirstname(), person.getLastname(),
                person.getGender(), person.getFatherID(), person.getMotherID(), person.getSpouseID());
        events = new ArrayList<>();
    }

    public ArrayList<EventExt> getEvents() {
        return events;
    }

    public void addEvent(EventExt event) {
        if (this.events.size() == 0) {
            this.events.add(event);
        } else {
            if (event.getType().toLowerCase().equals("birth")) {
                this.events.add(0, event);
            } else if (event.getType().toLowerCase().equals("death")) {
                this.events.add(event);
            } else {
                for (int i = 0; i < this.events.size(); i++) {
                    if (event.getYear() < this.events.get(i).getYear()) {
                        this.events.add(i, event);
                        return;
                    } else if (event.getYear() == this.events.get(i).getYear()) {
                        if (event.getType().compareTo(this.events.get(i).getType()) < 0) {
                            this.events.add(i, event);
                            return;
                        } else {
                            if (i == this.events.size() - 1) {
                                this.events.add(event);
                            } else {
                                this.events.add(i + 1, event);
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    public boolean search(String searchStr) {
        if (this.getFirstname().toLowerCase().contains(searchStr.toLowerCase()) ||
                this.getLastname().toLowerCase().contains(searchStr.toLowerCase())) {
            return true;
        } else {
            return false;
        }
    }

    public PersonExt getMother() {
        return mother;
    }

    public void setMother(PersonExt mother) {
        this.mother = mother;
    }

    public PersonExt getFather() {
        return father;
    }

    public void setFather(PersonExt father) {
        this.father = father;
    }

    public PersonExt getSpouse() {
        return spouse;
    }

    public void setSpouse(PersonExt spouse) {
        this.spouse = spouse;
    }

    public PersonExt getChild() {
        return child;
    }

    public void setChild(PersonExt child) {
        this.child = child;
    }
}
