package com.example.familymapclient.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Model.Event;
import Model.Person;
import Result.EventResult;
import Result.PersonResult;

public class DataCache {
    private static DataCache instance;

    private String authToken;
    private Person user;
    private PersonExt curUser;

    private Person[] persons;
    private Event[] events;
    private Map<String, PersonExt> personMap;
    private Map<String, PersonExt> fathersPersonMap;
    private Map<String, PersonExt> mothersPersonMap;
    private Map<String, EventExt> eventMap;
    private Map<String, EventExt> fathersEventMap;
    private Map<String, EventExt> mothersEventMap;
    private Set<String> eventTypes;
    private Map<String, Float> markerColors;

    private boolean lifeStoryLines;
    private boolean familyTreeLines;
    private boolean spouseLines;
    private boolean fathersSide;
    private boolean mothersSide;
    private boolean filterMale;
    private boolean filterFemale;

    public static DataCache getInstance() {
        if (instance == null) {
            instance = new DataCache();
        }
        return instance;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    private DataCache() {}

    public void loadData(PersonResult r) throws InvalidAuthTokenException {
        if (!r.isSuccess()) {
            throw new InvalidAuthTokenException("Error: Invalid Authorization.");
        } else {
            persons = r.getPersons();
        }
        this.personMap = new HashMap<>();
        for (int i = 0; i < persons.length; i++) {
            personMap.put(persons[i].getID(), new PersonExt(persons[i]));
        }
    }

    public void loadData(EventResult r) throws InvalidAuthTokenException {
        if (!r.isSuccess()) {
            throw new InvalidAuthTokenException("Error: Invalid Authorization.");
        } else {
            events = r.getEvents();
        }
        this.eventMap = new HashMap<>();
        this.eventTypes = new HashSet<>();
        for (int i = 0; i < events.length; i++) {
            eventMap.put(events[i].getID(), new EventExt(events[i]));
            this.eventTypes.add(events[i].getType());
        }
    }

    public void organize() {
        for (String id : personMap.keySet()) {
            PersonExt person = personMap.get(id);
            PersonExt father = personMap.get(person.getFatherID());
            PersonExt mother = personMap.get(person.getMotherID());
            PersonExt spouse = personMap.get(person.getSpouseID());
            person.setFather(father);
            person.setMother(mother);
            person.setSpouse(spouse);
            if (father != null) {
                father.setChild(person);
            }
            if (mother != null) {
                mother.setChild(person);
            }
        }

        for (String id : eventMap.keySet()) {
            EventExt event = eventMap.get(id);
            PersonExt person = personMap.get(event.getPerson_ID());
            event.setPerson(person);
            person.addEvent(event);
        }
        if (personMap.get(this.user.getID()) == null) {
            personMap.put(this.user.getID(), new PersonExt(this.user));
        }
        this.curUser = this.personMap.get(this.user.getID());

        this.fathersPersonMap = new HashMap<>();
        this.fathersPersonMap.put(this.curUser.getID(), this.curUser);
        this.fathersEventMap = new HashMap<>();
        for (EventExt event : this.curUser.getEvents()) {
            this.fathersEventMap.put(event.getID(), event);
        }
        splitFamily(this.curUser.getFather(), this.fathersPersonMap, this.fathersEventMap);

        this.mothersPersonMap = new HashMap<>();
        this.mothersPersonMap.put(this.curUser.getID(), this.curUser);
        this.mothersEventMap = new HashMap<>();
        for (EventExt event : this.curUser.getEvents()) {
            this.mothersEventMap.put(event.getID(), event);
        }
        splitFamily(this.curUser.getMother(), this.mothersPersonMap, this.mothersEventMap);

        this.lifeStoryLines = true;
        this.familyTreeLines = true;
        this.spouseLines = true;
        this.fathersSide = true;
        this.mothersSide = true;
        this.filterMale = true;
        this.filterFemale = true;
    }

    private void splitFamily(PersonExt root, Map<String, PersonExt> sidePersonMap, Map<String, EventExt> sideEventMap) {
        sidePersonMap.put(root.getID(), root);
        List<EventExt> personEvents = root.getEvents();
        for (EventExt event : personEvents) {
            sideEventMap.put(event.getID(), event);
        }

        if (root.getFather() != null) {
            splitFamily(root.getFather(), sidePersonMap, sideEventMap);
        }
        if (root.getMother() != null) {
            splitFamily(root.getMother(), sidePersonMap, sideEventMap);
        }
    }

    public Map<String, EventExt> getEvents() {
        Map<String, EventExt> map = new HashMap<>();
        if (this.curUser.getSpouse() != null) {
            for (int i = 0; i < this.curUser.getSpouse().getEvents().size(); i++) {
                filterGenderEvents(this.curUser.getSpouse().getEvents().get(i), map);
            }
        }
        if (this.fathersSide) {
            for (String id : this.fathersEventMap.keySet()) {
                filterGenderEvents(this.fathersEventMap.get(id), map);
            }
        }
        if (this.mothersSide) {
            for (String id : this.mothersEventMap.keySet()) {
                filterGenderEvents(this.mothersEventMap.get(id), map);
            }
        }
        if (!this.fathersSide && !this.mothersSide) {
            for (int i = 0; i < this.curUser.getEvents().size(); i++) {
                filterGenderEvents(this.curUser.getEvents().get(i), map);
            }
        }
        return map;
    }

    public Map<String, PersonExt> getPersons() {
        Map<String, PersonExt> map = this.personMap;
        return map;
    }

    private void filterGenderEvents(EventExt event, Map<String, EventExt> map) {
        switch (event.getPerson().getGender().toLowerCase()) {
            case "m":
                if (filterMale) {
                    map.put(event.getID(), event);
                }
                break;
            case "f":
                if (filterFemale) {
                    map.put(event.getID(), event);
                }
                break;
            default: break;
        }
    }

    private void filterGenderPersons(PersonExt person, Map<String, PersonExt> map) {
        switch (person.getGender().toLowerCase()) {
            case "m":
                if (filterMale) {
                    map.put(person.getID(), person);
                }
                break;
            case "f":
                if (filterFemale) {
                    map.put(person.getID(), person);
                }
                break;
            default: break;
        }
    }

    public Set<String> getEventTypes() {
        return this.eventTypes;
    }

    public PersonExt getPerson(String id) {
        return this.personMap.get(id);
    }

    public EventExt getEvent(String id) {
        return this.eventMap.get(id);
    }

    public PersonExt getUser() {
        return this.curUser;
    }

    public Map<String, Float> getMarkerColors() {
        return this.markerColors;
    }

    public void setMarkerColors(Map<String, Float> markerColors) {
        this.markerColors = markerColors;
    }

    public boolean isLifeStoryLines() {
        return lifeStoryLines;
    }

    public void setLifeStoryLines(boolean lifeStoryLines) {
        this.lifeStoryLines = lifeStoryLines;
    }

    public boolean isFamilyTreeLines() {
        return familyTreeLines;
    }

    public void setFamilyTreeLines(boolean familyTreeLines) {
        this.familyTreeLines = familyTreeLines;
    }

    public boolean isSpouseLines() {
        return spouseLines;
    }

    public void setSpouseLines(boolean spouseLines) {
        this.spouseLines = spouseLines;
    }

    public boolean isFathersSide() {
        return fathersSide;
    }

    public void setFathersSide(boolean fathersSide) {
        this.fathersSide = fathersSide;
    }

    public boolean isMothersSide() {
        return mothersSide;
    }

    public void setMothersSide(boolean mothersSide) {
        this.mothersSide = mothersSide;
    }

    public boolean isFilterMale() {
        return filterMale;
    }

    public void setFilterMale(boolean filterMale) {
        this.filterMale = filterMale;
    }

    public boolean isFilterFemale() {
        return filterFemale;
    }

    public void setFilterFemale(boolean filterFemale) {
        this.filterFemale = filterFemale;
    }

    public ArrayList<PersonExt> searchPersons(String searchStr) {
        ArrayList<PersonExt> persons = new ArrayList<>();
        Map<String, PersonExt> map = this.getPersons();
        for (String id : map.keySet()) {
            if (map.get(id).search(searchStr)) {
                persons.add(map.get(id));
            }
        }
        return persons;
    }

    public ArrayList<EventExt> searchEvents(String searchStr) {
        ArrayList<EventExt> events = new ArrayList<>();
        Map<String, EventExt> map = this.getEvents();
        for (String id : map.keySet()) {
            if (map.get(id).search(searchStr)) {
                events.add(map.get(id));
            }
        }
        return events;
    }

    public void clear() {
        this.authToken = null;
        this.curUser = null;
        this.user = null;
        this.persons = null;
        this.events = null;
        this.eventMap = null;
        this.personMap = null;
        this.eventTypes = null;
        instance = null;
    }
}
