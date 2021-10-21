package DAO;

public class UserDoesNotExistException extends Exception {
    public UserDoesNotExistException(String message) { super(message); }
    public UserDoesNotExistException() { super(); }

}
