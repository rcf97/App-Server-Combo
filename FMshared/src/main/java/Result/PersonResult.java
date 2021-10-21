package Result;

import Model.Person;

/**
 * Result object of Person query.
 */
public class PersonResult extends Result {
    /**
     * Person object returned from database query.
     */
    private Person person;

    /**
     * Person Array returned from family query.
     */
    private Person[] persons;

    /**
     * If result contains an array or not.
     */
    private boolean array;

    /**
     * Constructs a new successful Person query result.
     *
     * @param person Person returned from database query.
     */
    public PersonResult(Person person) {
        this.person = person;
        this.success = true;
        this.array = false;
    }

    /**
     * Constructs a new successful Person query result.
     *
     * @param persons Person Array returned from database query.
     */
    public PersonResult(Person[] persons) {
        this.persons = persons;
        this.array = true;
        this.success = true;
    }

    /**
     * Constructs a new failed Person Query result.
     *
     * @param message Error message of query.
     */
    public PersonResult(String message) {
        this.message = message;
        this.success = false;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public boolean isArray() {
        return this.array;
    }

    public void setArray(boolean array) {
        this.array = array;
    }
}
