package Request;

/**
 * Request object to register a new user.
 */
public class RegisterRequest extends Request {
    /**
     * Name of user to be created.
     */
    private String userName;

    /**
     * Password of user to be created.
     */
    private String password;

    /**
     * Email of user to be created.
     */
    private String email;

    /**
     * First name of new user.
     */
    private String firstName;

    /**
     * Last name of new user.
     */
    private String lastName;

    /**
     * Gender of new user (either "m" or "f").
     */
    private String gender;

    /**
     * Constructs new Request for a new user.
     * @param username Name of user to be created.
     * @param password Password of user to be created.
     * @param email Email of user to be created.
     * @param firstName First name of new user.
     * @param lastName Last name of new user.
     * @param gender Gender of new user (either "m" or "f").
     */
    public RegisterRequest(String username, String password, String email,
                           String firstName, String lastName, String gender) {
        this.userName = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
