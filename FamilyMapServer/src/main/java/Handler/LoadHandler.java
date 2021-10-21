package Handler;

import DAO.DataAccessException;
import Model.Event;
import Model.Person;
import Model.User;
import Request.LoadRequest;
import Request.RegisterRequest;
import Result.LoadResult;
import Result.RegisterResult;
import Service.LoadService;
import Service.RegisterService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class LoadHandler extends RequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);
                LoadService loadService = new LoadService();
                LoadRequest request = this.parse(reqData);
                LoadResult resData = loadService.load(request);

                OutputStream respBody = exchange.getResponseBody();
                if (resData.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }

                writeString(generateJson(resData).toString(), respBody);
                respBody.close();
            }
        } catch (DataAccessException | IOException ex) {

        }
    }

    private LoadRequest parse(String jsonBody) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonBody, JsonObject.class);
        JsonArray jsonUsers = jsonObject.getAsJsonArray("users");
        JsonArray jsonPersons = jsonObject.getAsJsonArray("persons");
        JsonArray jsonEvents = jsonObject.getAsJsonArray("events");
        User[] users = gson.fromJson(jsonUsers, User[].class);
        Person[] persons = gson.fromJson(jsonPersons, Person[].class);
        Event[] events = gson.fromJson(jsonEvents, Event[].class);
        return new LoadRequest(users, persons, events);
    }

    private JSONObject generateJson(LoadResult r) {
        JSONObject rAttributes = new JSONObject();
        rAttributes.put("message", r.getMessage());
        rAttributes.put("success", r.isSuccess());
        return rAttributes;
    }
}
