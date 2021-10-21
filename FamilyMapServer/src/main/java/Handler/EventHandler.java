package Handler;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import Model.AuthToken;
import Model.Event;
import Result.EventResult;
import Service.EventService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class EventHandler extends RequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Database db = new Database();
        EventResult res = null;
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                String url = exchange.getRequestURI().toString();
                url = url.substring(6);
                if (!url.equals("") && url.charAt(url.length() - 1) == ('/')) {
                    url = url.substring(0, url.length() - 1);
                }
                if (!url.equals("") && url.charAt(0) == ('/')) {
                    url = url.substring(1);
                }

                EventService eventService = new EventService();
                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {
                    String token = reqHeaders.getFirst("Authorization");
                    AuthTokenDAO aDao = new AuthTokenDAO(db.openConnection());
                    AuthToken authToken = aDao.find(token);
                    db.closeConnection(true);

                    if (authToken == null) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        res = new EventResult("Error: Invalid Auth Token given.");
                    } else {
                        String username = authToken.getUser();
                        if (url.equals("")) {
                            res = eventService.events(username);
                            if (res.getEvents() == null) {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                                res = new EventResult("Error: No events associated with this user.");
                            }
                        } else {
                            res = eventService.eventQuery(url);
                            if (res.getEvent() == null) {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                                res = new EventResult("Error: Requested event does not exist.");
                            }
                            if (!res.getEvent().getUser().equals(username)) {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                                res = new EventResult("Error: Requested event does not match user.");
                            }
                        }
                        if (res.isSuccess()) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        }
                    }
                } else {
                    res = new EventResult("Error: No Auth Token given.");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
            }
        } catch (Exception ex) {
            res = new EventResult(ex.getMessage());
            try {
                db.closeConnection(false);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
        OutputStream respBody = exchange.getResponseBody();
        writeString(generateJson(res).toString(), respBody);
        exchange.close();
    }

    private JSONObject generateJson(EventResult r) {
        JSONObject rAttributes = new JSONObject();
        if (r.isSuccess()) {
            if (r.isArray()) {
                JSONArray array = new JSONArray();
                for (int i = 0; i < r.getEvents().length; i++) {
                    array.put(generateEventJson(r.getEvents()[i]));
                }
                rAttributes.put("data", array);
            } else {
                rAttributes = generateEventJson(r.getEvent());
            }
        } else {
            rAttributes.put("message", r.getMessage());
        }
        rAttributes.put("success", r.isSuccess());
        return rAttributes;
    }

    private JSONObject generateEventJson(Event e) {
        JSONObject event = new JSONObject();
        event.put("associatedUsername", e.getUser());
        event.put("eventID", e.getID());
        event.put("personID", e.getPerson_ID());
        event.put("latitude", e.getLatitude());
        event.put("longitude", e.getLongitude());
        event.put("country", e.getCountry());
        event.put("city", e.getCity());
        event.put("eventType", e.getType());
        event.put("year", e.getYear());

        return event;
    }
}
