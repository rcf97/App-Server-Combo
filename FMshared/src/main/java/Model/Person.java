package Model;

/**
 * Person object stores the Family History info from a person.
 */
public class Person extends Object {
    /**
     * Unique identifier for this person.
     */
    private String personID;

    /**
     * User to which this person belongs.
     */
    private String associatedUsername;

    /**
     * Person's first name.
     */
    private String firstName;

    /**
     * Person's last name.
     */
    private String lastName;

    /**
     * Person's gender.
     */
    private String gender;

    /**
     * Person ID of person's father (possibly null).
     */
    private String fatherID;

    /**
     * Person ID of person's mother (possibly null).
     */
    private String motherID;

    /**
     * Person ID of person's spouse (possibly null).
     */
    private String spouseID;

    /**
     * Constructs a Person object.
     *
     * @param id ID associated with this Person.
     * @param username Username associated with this Person.
     * @param firstname Person's first name.
     * @param lastname Person's last name.
     * @param gender Person's gender (Either "m" or "f").
     * @param fatherid ID of the father of this Person.
     * @param motherid ID of the mother of this Person.
     * @param spouseid ID of the spouse of this Person.
     */
    public Person(String id, String username, String firstname, String lastname, String gender, String fatherid,
                  String motherid, String spouseid) {
        this.personID = id;
        this.associatedUsername = username;
        this.firstName = firstname;
        this.lastName = lastname;
        this.gender = gender;
        this.fatherID = fatherid;
        this.motherID = motherid;
        this.spouseID = spouseid;
    }

    /**
     * Constructs a Person object.
     * @param username Username associated with this Person.
     * @param firstname Person's first name.
     * @param lastname Person's last name.
     * @param gender Person's gender (Either "m" or "f").
     */
    public Person(String username, String firstname, String lastname, String gender) {

    }

    /**
     * Method to get the Person ID.
     * @return Person ID
     */
    public String getID() {
        return this.personID;
    }

    public void setId(String id) {
        this.personID = id;
    }

    public String getUsername() {
        return associatedUsername;
    }

    public void setUsername(String username) {
        this.associatedUsername = username;
    }

    public String getFirstname() {
        return firstName;
    }

    public void setFirstname(String firstname) {
        this.fatherID = firstname;
    }

    public String getLastname() {
        return lastName;
    }

    public void setLastname(String lastname) {
        this.lastName = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherid) {
        this.fatherID = fatherid;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherid) {
        this.motherID = motherid;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseid) {
        this.spouseID = spouseid;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Person person = (Person) obj;
        if (!this.personID.equals(person.personID)) {
            return false;
        }
        if (!this.associatedUsername.equals(person.associatedUsername)) {
            return false;
        }
        if (!this.firstName.equals(person.firstName)) {
            return false;
        }
        if (!this.lastName.equals(person.lastName)) {
            return false;
        }
        if (!this.gender.equals(person.gender)) {
            return false;
        }
        if (!this.fatherID.equals(person.fatherID)) {
            return false;
        }
        if (!this.motherID.equals(person.motherID)) {
            return false;
        }
        if (!this.spouseID.equals(person.spouseID)) {
            return false;
        }
        return true;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
