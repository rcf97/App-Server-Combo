package Service;

import DAO.*;
import Model.User;
import Result.FillResult;

import static java.lang.Math.pow;

/**
 * Populates the server's database with generated data for the specified user name.
 * The required "username" parameter must be a user already registered with the server.
 * If there is any data in the database already associated with the given user name,
 * it is deleted.  The optional "generations" parameter lets the caller
 * specify the number of generations of ancestors to be generated, and must
 * be a non-negative integer (the default is 4, which results in 31 new persons
 * each with associated events).
 */
public class FillService {
    /**
     * Fills the database with the specified number of generations.
     *
     * @param userName of user for whom to fill ancestral data.
     * @param generations of data to fill.
     * @return FillResult object with the fill result.
     */
    public FillResult fill(String userName, int generations) throws DataAccessException {
        Database db = new Database();
        FillResult res = null;
        try {
            UserDAO uDao = new UserDAO(db.openConnection());
            User test = uDao.find(userName);
            db.closeConnection(true);
            if (test == null) {
                return new FillResult("User does not exist.", false);
            }
            PersonDAO pDao = new PersonDAO(db.openConnection());
            pDao.clear(userName);
            db.closeConnection(true);
            EventDAO eDao = new EventDAO(db.openConnection());
            eDao.clear(userName);
            db.closeConnection(true);

            GenerateService gen = new GenerateService();
            gen.generate(test.getUsername(), test.getFirstname(), test.getLastname(), test.getGender(),
                    test.getPersonID(), generations);
            int numPersons = (int) pow(2, generations + 1) - 1;
            int numEvents = (int) (3 * numPersons) - 2;
            String message = "Successfully added " + String.valueOf(numPersons) + " persons and " +
                    String.valueOf(numEvents) + " events to the database.";
            res = new FillResult(message, true);
        } catch(Exception ex) {
            db.closeConnection(false);
            res = new FillResult(ex.getMessage(), false);
        }
        return res;
    }
}
