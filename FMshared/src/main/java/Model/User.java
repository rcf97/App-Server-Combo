package Model;

/**
 * Represents a user of the application.
 */
public class User extends Object {
    /**
     * Unique user name.
     */
    private String userName;

    /**
     * User's password.
     */
    private String password;

    /**
     * User's email address.
     */
    private String email;

    /**
     * First name of User.
     */
    private String firstName;

    /**
     * Last name of User.
     */
    private String lastName;

    /**
     * User's gender ("f" or "m").
     */
    private String gender;

    /**
     * Unique Person ID assigned to this user's Person object.
     */
    private String personID;

    /**
     * Constructs a new User object.
     *
     * @param username the username of the user.
     * @param password the password of the user.
     * @param email the email of the user.
     * @param firstname the first name of the user.
     * @param lastname the last name of the user.
     * @param gender the gender of the user.
     * @param personid ID of associated Person.
     */
    public User(String username, String password, String email,
                String firstname, String lastname, String gender, String personid) {
        this.userName = username;
        this.password = password;
        this.email = email;
        this.firstName = firstname;
        this.lastName = lastname;
        this.gender = gender;
        this.personID = personid;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstName;
    }

    public void setFirstname(String firstname) {
        this.firstName = firstname;
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

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personid) {
        this.personID = personid;
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
        User user = (User) obj;
        if (!this.userName.equals(user.userName)) {
            return false;
        }
        if (!this.password.equals(user.password)) {
            return false;
        }
        if (!this.email.equals(user.email)) {
            return false;
        }
        if (!this.firstName.equals(user.firstName)) {
            return false;
        }
        if (!this.lastName.equals(user.lastName)) {
            return false;
        }
        if (!this.gender.equals(user.gender)) {
            return false;
        }
        if (!this.personID.equals(user.personID)) {
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
