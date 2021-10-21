package Request;

/**
 * Request object to log in a user.
 */
public class LoginRequest extends Request {
    /**
     * Username of user who is trying to log in.
     */
    private String userName;

    /**
     * Password of user who is trying to log in.
     */
    private String password;

    /**
     * Constructs a new request for login authorization.
     * @param userName Username of user who is trying to log in.
     * @param password Password of user who is trying to log in.
     */
    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
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
}
