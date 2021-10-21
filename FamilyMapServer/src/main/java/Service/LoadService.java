package Service;

import DAO.*;
import Model.Event;
import Model.Person;
import Model.User;
import Result.LoadResult;
import Request.LoadRequest;

/**
 * Clears all data from the database, and then loads the posted user,
 * person, and event data into the database.
 */
public class LoadService {
    /**
     * Clears data from the database, then loads the associated user,
     * person, and event data.
     *
     * @param r Request object with the required info.
     * @return RegisterResult object describing completion of the load.
     */
    public LoadResult load(LoadRequest r) throws DataAccessException {
        Database db = new Database();
        LoadResult res = null;
        try {
            db.openConnection();
            db.clearTables();
            db.closeConnection(true);

            UserDAO uDao = new UserDAO(db.openConnection());
            for (int i = 0; i < r.getUsers().length; i++) {
                User user = r.getUsers()[i];
                uDao.insert(user);
            }
            db.closeConnection(true);

            PersonDAO pDao = new PersonDAO(db.openConnection());
            for (int i = 0; i < r.getPersons().length; i++) {
                Person person = r.getPersons()[i];
                pDao.insert(person);
            }
            db.closeConnection(true);

            EventDAO eDao = new EventDAO(db.openConnection());
            for (int i = 0; i < r.getEvents().length; i++) {
                Event event = r.getEvents()[i];
                eDao.insert(event);
            }
            db.closeConnection(true);

            String message = "Successfully added " + String.valueOf(r.getUsers().length) + " users, " +
                    String.valueOf(r.getPersons().length) + " persons, and " + String.valueOf(r.getEvents().length) +
                    " events to the database.";
            res = new LoadResult(true, message);
        } catch (Exception ex) {
            db.closeConnection(false);
            res = new LoadResult(false, ex.getMessage());
        }
        return res;
    }
}
