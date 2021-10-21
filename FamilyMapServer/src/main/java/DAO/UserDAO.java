package DAO;

import Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Used to access and manipulate entries in the User table in the database.
 */
public class UserDAO {
    private final Connection conn;

    public UserDAO(Connection conn) { this.conn = conn; }

    /**
     * Insert a new User entry into the User table.
     *
     * @param user "User" object to insert into the table.
     * @throws DataAccessException if fails to connect to database.
     */
    public void insert(User user) throws DataAccessException, UserAlreadyExistsException {
        User compare = this.find(user.getUsername());
        if (compare != null) {
            if (compare.equals(user)) {
                throw new UserAlreadyExistsException("Error: Username already taken.");
            } else {
                throw new UserAlreadyExistsException("Error: User already exists.");
            }
        }

        String sql = "INSERT INTO Users (username, password, email, firstname, lastname, gender, " +
                "personid) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstname());
            stmt.setString(5, user.getLastname());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Find an entry in the table.
     *
     * @param username Username of user who is being searched.
     * @return Entry in table matching the parameters.
     * @throws DataAccessException if error occurs while finding event.
     */
    public User find(String username) throws DataAccessException {
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM Users WHERE username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("username"), rs.getString("password"),
                        rs.getString("email"), rs.getString("firstname"),
                        rs.getString("lastname"), rs.getString("gender"),
                        rs.getString("personid"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /**
     * Deletes all data in the User table.
     *
     * @throws DataAccessException if an error is encountered while connecting to the database.
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Users";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Error encountered while deleting data.");
        }
    }
}
