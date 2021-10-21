package DAO;

import Model.Event;
import Model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Used to access and manipulate entries in the Person table in the database.
 */
public class PersonDAO {
    private final Connection conn;

    public PersonDAO(Connection conn) {this.conn = conn;}

    /**
     * Insert a new Person entry into the Person table.
     *
     * @param person "Person" object to insert into the table.
     * @throws DataAccessException if connection to database fails.
     */
    public void insert(Person person) throws DataAccessException {
        String sql = "INSERT INTO Persons (id, username, firstname, lastname, gender, " +
                "fatherid, motherid, spouseid) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, person.getID());
            stmt.setString(2, person.getUsername());
            stmt.setString(3, person.getFirstname());
            stmt.setString(4, person.getLastname());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Error encountered while inserting into the database.");
        }

    }

    /**
     * Find an entry in the table.
     *
     * @param PersonID ID of person who is being searched.
     * @return Entry in table matching the parameters.
     * @throws DataAccessException if encounters error while finding person.
     */
    public Person find(String PersonID) throws DataAccessException {
        Person person = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE id = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, PersonID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("id"), rs.getString("username"),
                        rs.getString("firstname"), rs.getString("lastname"),
                        rs.getString("gender"), rs.getString("fatherid"),
                        rs.getString("motherid"), rs.getString("spouseid"));
                return person;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DataAccessException("Error encountered while finding person.");
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
     * Find all family members of specified user.
     *
     * @param username Username of user for whom we are finding.
     * @return Array of the family members of this user.
     */
    public Person[] findPersons(String username) throws DataAccessException {
        ArrayList<Person> persons = new ArrayList<>();
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                person = new Person(rs.getString("id"), rs.getString("username"),
                        rs.getString("firstname"), rs.getString("lastname"),
                        rs.getString("gender"), rs.getString("fatherid"),
                        rs.getString("motherid"), rs.getString("spouseid"));
                persons.add(person);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DataAccessException("Error encountered while finding persons.");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        if (persons.size() == 0) {
            return null;
        }
        Person[] family = new Person[persons.size()];
        for (int i = 0; i < persons.size(); i++) {
            family[i] = persons.get(i);
        }
        return family;
    }

    /**
     * Deletes all data in the Person table.
     *
     * @throws DataAccessException if an error is encountered while connecting to the database.
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Persons";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Error encountered while deleting data.");
        }
    }

    /**
     * Deletes all data in the Person table associated with the given user.
     *
     * @param userName of user for whom we are deleting data.
     * @throws DataAccessException if an error is encountered while connecting to the database.
     */
    public void clear(String userName) throws DataAccessException {
        String sql = "DELETE FROM Persons WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting data.");
        }
    }
}
