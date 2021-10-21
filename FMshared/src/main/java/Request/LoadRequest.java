package Request;

import Model.Event;
import Model.Person;
import Model.User;

/**
 * Request to load database with family history info.
 */
public class LoadRequest extends Request {
    /**
     * Array of users to be created.
     */
    private User[] users;

    /**
     * Person info for the users.
     */
    private Person[] persons;

    /**
     * Event info for the users.
     */
    private Event[] events;

    /**
     * Constructs a new Load Request object.
     *
     * @param users User info to be created.
     * @param persons Person info for these users.
     * @param events Event info for these users.
     */
    public LoadRequest(User[] users, Person[] persons, Event[] events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }
}
