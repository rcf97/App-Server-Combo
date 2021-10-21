package Model;

/**
 * Stores the info for various events in a person's life.
 */
public class Event extends Object {
    /**
     * Unique identifier for this event.
     */
    private String eventID;

    /**
     * User (username) to which this person belongs.
     */
    private String associatedUsername;

    /**
     * ID of person to which this event belongs.
     */
    private String personID;

    /**
     * Latitude of event's location.
     */
    private float latitude;

    /**
     * Longitude of event's location.
     */
    private float longitude;

    /**
     * Country in which event occurred.
     */
    private String country;

    /**
     * City in which event occurred.
     */
    private String city;

    /**
     * Type of event (birth, baptism, christening, marriage, death, etc.).
     */
    private String eventType;

    /**
     * Year in which event occurred.
     */
    private int year;

    /**
     * Constructs Event object.
     * @param id Event ID.
     * @param user User (username) to which this person belongs.
     * @param person_id ID of person to which this event belongs.
     * @param latitude Latitude of event's location.
     * @param longitude Longitude of event's location.
     * @param country Country in which event occurred.
     * @param city City in which event occurred.
     * @param type Type of event (birth, baptism, christening, marriage, death, etc.).
     * @param year Year in which event occurred.
     */
    public Event(String id, String user, String person_id, float latitude, float longitude,
                 String country, String city, String type, int year) {
        this.eventID = id;
        this.associatedUsername = user;
        this.personID = person_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = type;
        this.year = year;
    }

    public String getID() {
        return eventID;
    }

    public void setID(String id) {
        this.eventID = id;
    }

    public String getUser() {
        return associatedUsername;
    }

    public void setUser(String user) {
        this.associatedUsername = user;
    }

    public String getPerson_ID() {
        return personID;
    }

    public void setPerson_ID(String person_id) {
        this.personID = person_id;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getType() {
        return eventType;
    }

    public void setType(String type) {
        this.eventType = type;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof Event) {
            Event oEvent = (Event) obj;
            return oEvent.getID().equals(getID()) &&
                    oEvent.getUser().equals(getUser()) &&
                    oEvent.getPerson_ID().equals(getPerson_ID()) &&
                    oEvent.getLatitude() == (getLatitude()) &&
                    oEvent.getLongitude() == (getLongitude()) &&
                    oEvent.getCountry().equals(getCountry()) &&
                    oEvent.getCity().equals(getCity()) &&
                    oEvent.getType().equals(getType()) &&
                    oEvent.getYear() == (getYear());
        } else {
            return false;
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
