package DAO;

import Model.Event;
import Model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Used to access and manipulate entries in the Event table in the database.
 */
public class EventDAO {
    private final Connection conn;

    public EventDAO(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Insert a new Event entry into the Event table.
     *
     * @param event "Event" object to insert into the table.
     * @throws DataAccessException if fails to connect to database.
     */
    public void insert(Event event) throws DataAccessException {
        String sql = "INSERT INTO Events (id, user, person_id, latitude, longitude, " +
                "country, city, type, year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, event.getID());
            stmt.setString(2, event.getUser());
            stmt.setString(3, event.getPerson_ID());
            stmt.setDouble(4, event.getLatitude());
            stmt.setDouble(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Find an entry in the table.
     *
     * @param eventID ID of the event to find.
     * @return Entry in table matching the parameters.
     * @throws DataAccessException if unable to connect to database, throws error.
     */
    public Event find(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE id = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("id"), rs.getString("user"),
                        rs.getString("person_id"), rs.getFloat("latitude"),
                        rs.getFloat("longitude"), rs.getString("country"),
                        rs.getString("city"), rs.getString("type"),
                        rs.getInt("year"));
                return event;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
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

    public Event[] findEvents(String username) throws DataAccessException {
        ArrayList<Event> events = new ArrayList<>();
        Event event = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM events WHERE user = ?";
        try (PreparedStatement stmt =  conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                event = new Event(rs.getString("id"), rs.getString("user"),
                        rs.getString("person_id"), rs.getFloat("latitude"),
                        rs.getFloat("longitude"), rs.getString("country"),
                        rs.getString("city"), rs.getString("type"),
                        rs.getInt("year"));
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if (events.size() == 0) {
            return null;
        }
        Event[] array = new Event[events.size()];
        for (int i = 0; i < events.size(); i++) {
            array[i] = events.get(i);
        }
        return array;
    }

    /**
     * Deletes all data in the Events table.
     *
     * @throws DataAccessException if an error is encountered while connecting to the database.
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Events";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Error encountered while deleting data.");
        }
    }

    /**
     * Deletes all data in the Event table associated with the given user.
     *
     * @param userName of user for whom we are deleting data.
     * @throws DataAccessException if an error is encountered while connecting to the database.
     */
    public void clear(String userName) throws DataAccessException {
        String sql = "DELETE FROM Events WHERE user = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting data.");
        }
    }
}
