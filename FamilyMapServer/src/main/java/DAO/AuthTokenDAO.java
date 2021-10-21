package DAO;

import Model.AuthToken;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Used to access and manipulate entries in the AuthToken table in the database.
 */
public class AuthTokenDAO {
    private final Connection conn;

    public AuthTokenDAO(Connection conn) { this.conn = conn; }

    /**
     * Insert a new AuthToken entry into the AuthToken table.
     *
     * @param authToken "AuthToken" object to insert into the table.
     * @throws DataAccessException if error encountered while connecting to database.
     */
    public void insert(AuthToken authToken) throws DataAccessException {
        String sql = "INSERT INTO authtokens (id, user) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken.getID());
            stmt.setString(2, authToken.getUser());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Error encountered while inserting into the database.");
        }
    }

    /**
     * Find an entry in the table matching the ID given.
     *
     * @param id ID of AuthToken to query the database for.
     * @return Entry in table matching the parameters.
     * @throws DataAccessException if error encountered while searching for AuthToken.
     */
    public AuthToken find(String id) throws DataAccessException {
        AuthToken token;
        ResultSet rs = null;
        String sql = "SELECT * FROM authtokens WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                token = new AuthToken(rs.getString("id"), rs.getString("user"));
                return token;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DataAccessException("Error encountered while searching database.");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Deletes all data in the AuthToken table.
     *
     * @throws DataAccessException if an error is encountered while connecting to the database.
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM authtokens";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Error encountered while deleting data.");
        }
    }
}
