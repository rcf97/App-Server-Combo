package Model;

/**
 * Authorization Token for logged-in user.
 */
public class AuthToken extends Object{
    /**
     * Unique authorization token string.
     */
    private String id;

    /**
     * User associated with this authorization token.
     */
    private String user;

    /**
     * Contructs a new Authorization Token.
     *
     * @param id Token id.
     * @param user User with which the Authorization Token is associated.
     */
    public AuthToken(String id, String user) {
        this.id = id;
        this.user = user;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof AuthToken) {
            AuthToken a = (AuthToken) obj;
            return a.getID().equals(this.getID()) &&
                    a.getUser().equals(this.getUser());
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
