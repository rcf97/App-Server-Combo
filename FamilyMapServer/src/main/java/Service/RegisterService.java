package Service;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import Model.AuthToken;
import Model.Person;
import Model.User;
import Request.RegisterRequest;
import Result.RegisterResult;

import java.sql.Connection;

import static Service.GenerateService.UUIDGenerator;

/**
 * Creates a new user account, generates 4 generations of ancestor data for the
 * new user, logs the user in, and returns an authorization token.
 */
public class RegisterService extends Service {
    /**
     * Registers a new user.
     *
     * @param r RegisterRequest object to register new user.
     * @return RegisterResult object of the registration result.
     */
    public RegisterResult register(RegisterRequest r) throws DataAccessException {
        RegisterResult res = null;
        Database db = new Database();
        try {
            GenerateService gen = new GenerateService();
            String personid = gen.generate(r.getUserName(), r.getFirstName(), r.getLastName(), r.getGender(),
                    UUIDGenerator(Person.class), 4);
            User newUser = new User(r.getUserName(), r.getPassword(), r.getEmail(),
                    r.getFirstName(), r.getLastName(), r.getGender(), personid);

            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.insert(newUser);
            db.closeConnection(true);
            String authorization = UUIDGenerator(AuthToken.class);
            AuthToken authToken = new AuthToken(authorization, r.getUserName());
            AuthTokenDAO aDao = new AuthTokenDAO(db.openConnection());
            aDao.insert(authToken);
            db.closeConnection(true);

            res = new RegisterResult(authorization, r.getUserName(), personid);
        } catch(Exception ex) {
            res = new RegisterResult(ex.getMessage());
            db.closeConnection(false);
        }
        return res;
    }
}
