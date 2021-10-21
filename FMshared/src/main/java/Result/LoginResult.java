package Result;

import Model.AuthToken;

/**
 * Result object with info about the user login.
 */
public class LoginResult extends Result {
    /**
     * Authorization Token associated with the log-in.
     */
    private String authToken;

    /**
     * Username for User.
     */
    private String userName;

    /**
     * Person ID associated with this User.
     */
    private String personID;

    /**
     * Constructs a failed login report.
     * @param message Error message describing the error.
     */
    public LoginResult(String message) {
        this.message = message;
        this.success = false;
    }

    /**
     * Constructs a new successful login report.
     * @param authToken Authorization Token associated with the login.
     * @param userName Username for User.
     * @param personID Person ID associated with this User.
     */
    public LoginResult(String authToken, String userName, String personID) {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
        this.success = true;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    @Override
    public String toString() {
        String str = "{\n";
        if (this.success) {
            str += ("\t\"authToken\":\"" + this.authToken + "\",\n");
            str += ("\t\"userName\":\"" + this.userName + "\",\n");
            str += ("\t\"personID\":\"" + this.personID + "\",\n");
        } else {
            str += ("\t\"message\":\"" + this.message + "\",\n");
        }
        str += ("\t\"success\":\"" + this.success + "\",\n");
        str += "}";
        return str;
    }
}
