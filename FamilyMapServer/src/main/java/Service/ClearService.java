package Service;

import DAO.DataAccessException;
import DAO.Database;
import Result.ClearResult;

/**
 * Deletes all data from the database, including user accounts,
 * auth tokens, and generated person and event data.
 */
public class ClearService {
    /**
     * Clears all data from the database.
     *
     * @return ClearResult object of the clear result.
     */
    public ClearResult clear() throws DataAccessException {
        Database db = new Database();
        ClearResult res = null;
        try {
            db.openConnection();
            db.clearTables();
            db.closeConnection(true);
            res = new ClearResult("Clear succeeded.", true);
        } catch (Exception ex) {
            res = new ClearResult(ex.getMessage(), false);
            db.closeConnection(false);
        }
        return res;
    }
}
