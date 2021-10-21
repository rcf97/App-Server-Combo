package Service;

import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDAO;
import Model.Event;
import Result.EventResult;
import Result.PersonResult;

/**
 * Returns Event objects based on the search queries.
 */
public class EventService {
    /**
     * Returns all events for all family members of the current user.
     * The current user is determined from the provided auth token.
     *
     * @param username Username of user for whom we are searching.
     * @return Result object with the array with the Event objects.
     */
    public EventResult events(String username) throws DataAccessException {
        EventResult res = null;
        Database db = new Database();
        try {
            EventDAO eDao = new EventDAO(db.openConnection());
            Event[] events = eDao.findEvents(username);
            db.closeConnection(true);

            res = new EventResult(events);
        } catch (Exception ex) {
            res = new EventResult(ex.getMessage());
            db.closeConnection(false);
        }
        return res;
    }

    /**
     * Returns the single Event object with the specified ID.
     *
     * @param eventid Request including the info for the query.
     * @return EventResult describing the Result of the query.
     */
    public EventResult eventQuery(String eventid) throws DataAccessException {
        EventResult res = null;
        Database db = new Database();
        try {
            EventDAO eDao = new EventDAO(db.openConnection());
            Event event = eDao.find(eventid);
            db.closeConnection(true);

            res = new EventResult(event);
        } catch (Exception ex) {
            res = new EventResult(ex.getMessage());
            db.closeConnection(false);
        }
        return res;
    }
}
