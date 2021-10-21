package DAO;

public class UserNameAlreadyExistsException extends Exception {
    UserNameAlreadyExistsException(String message) { super(message); }
    UserNameAlreadyExistsException() { super(); }
}
