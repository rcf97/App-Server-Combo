package Service;

import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import Model.Person;
import Result.PersonResult;

/**
 * Returns the Person objects based on the search queries.
 */
public class PersonService {
    /**
     * Returns all family members of the current user.
     * The current user is determined from the provided authToken.
     *
     * @param username Username of user who is searching.
     * @return Array with the Person objects (this user's family).
     */
    public PersonResult person(String username) throws DataAccessException {
        PersonResult res = null;
        Database db = new Database();
        try {
            PersonDAO pDao = new PersonDAO(db.openConnection());
            Person[] persons = pDao.findPersons(username);
            db.closeConnection(true);

            res = new PersonResult(persons);
        } catch (Exception ex) {
            res = new PersonResult(ex.getMessage());
            db.closeConnection(false);
        }
        return res;
    }

    /**
     * Returns the single Person object with the specified ID.
     *
     * @param personid Request including the info for the query.
     * @return PersonResult describing the Result of the query.
     */
    public PersonResult personQuery(String personid) throws DataAccessException {
        PersonResult res = null;
        Database db = new Database();
        try {
            PersonDAO pDao = new PersonDAO(db.openConnection());
            Person person = pDao.find(personid);
            db.closeConnection(true);

            res = new PersonResult(person);
        } catch (Exception ex) {
            res = new PersonResult(ex.getMessage());
            db.closeConnection(false);
        }
        return res;
    }
}
