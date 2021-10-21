package Service;

import DAO.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Request.RegisterRequest;
import Result.RegisterResult;
import com.google.gson.Gson;

import java.sql.Connection;
import java.util.UUID;
import java.util.Random;

public abstract class Service {
    public abstract RegisterResult register(RegisterRequest r) throws DataAccessException;

}
