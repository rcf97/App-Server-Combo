package DAO;

public class UserAlreadyExistsException extends Exception {
    UserAlreadyExistsException(String message) { super(message); }
    UserAlreadyExistsException() { super(); }
}
