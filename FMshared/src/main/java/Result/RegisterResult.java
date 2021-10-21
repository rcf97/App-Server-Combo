package Result;

import Model.AuthToken;

/**
 * Result object with info about the registration of a new user.
 */
public class RegisterResult extends LoginResult {

    /**
     * Constructs a new failed registration report
     * @param message message describing error
     */
    public RegisterResult(String message) {
        super(message);
    }

    /**
     * Constructs a new successful registration report.
     * @param authToken Authorization Token associated with the new User.
     * @param userName Username for new User.
     * @param personID Person ID associated with this new User.
     */
    public RegisterResult(String authToken, String userName, String personID) {
        super(authToken, userName, personID);
    }
}
