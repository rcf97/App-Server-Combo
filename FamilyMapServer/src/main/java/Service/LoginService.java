package Service;

import DAO.*;
import Model.AuthToken;
import Model.User;
import Request.LoginRequest;
import Result.LoginResult;

import static Service.GenerateService.UUIDGenerator;

/**
 * Logs in the user and returns an authorization token.
 */
public class LoginService {
    /**
     * Logs a user into the system.
     *
     * @param r LoginRequest object with info to log in the user.
     * @return LoginResult object with result of login.
     */
    public LoginResult login(LoginRequest r) throws Exception {
        Database db = new Database();
        LoginResult res = null;
        User test = null;
        try {
            UserDAO uDao = new UserDAO(db.openConnection());
            test = uDao.find(r.getUserName());
            db.closeConnection(true);

            AuthTokenDAO aDao = new AuthTokenDAO(db.openConnection());
            String authToken = null;
            if (test == null) {
                throw new UserDoesNotExistException("Error: User does not exist.");
            } else {
                if (!r.getPassword().equals(test.getPassword())) {
                    throw new IncorrectPasswordException("Error: Password does not match.");
                }
                else {
                    authToken = UUIDGenerator(AuthToken.class);
                    AuthToken add = new AuthToken(authToken, r.getUserName());
                    aDao.insert(add);
                    res = new LoginResult(authToken, test.getUsername(), test.getPersonID());
                }
            }
            db.closeConnection(true);
        } catch (Exception ex) {
            res = new LoginResult(ex.getMessage());
            db.closeConnection(false);
        }
        return res;
    }
}
