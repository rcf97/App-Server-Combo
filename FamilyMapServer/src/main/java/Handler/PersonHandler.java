package Handler;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import Model.AuthToken;
import Model.Person;
import Result.PersonResult;
import Service.PersonService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class PersonHandler extends RequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Database db = new Database();
        PersonResult res = null;
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                String url = exchange.getRequestURI().toString();
                url = url.substring(7);
                if (!url.equals("") && url.charAt(url.length() - 1) == ('/')) {
                    url = url.substring(0, url.length() - 1);
                }
                if (!url.equals("") && url.charAt(0) == ('/')) {
                    url = url.substring(1, url.length());
                }

                PersonService personService = new PersonService();
                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {
                    String token = reqHeaders.getFirst("Authorization");
                    AuthTokenDAO aDao = new AuthTokenDAO(db.openConnection());
                    AuthToken authToken = aDao.find(token);
                    db.closeConnection(true);

                    if (authToken == null) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        res = new PersonResult("Error: Invalid Auth Token given.");
                    } else {
                        String username = authToken.getUser();
                        if (url.equals("")) {
                            res = personService.person(username);
                            if (res.getPersons() == null) {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                                res = new PersonResult("Error: No persons associated with this user.");
                            }
                        } else {
                            res = personService.personQuery(url);
                            if (res.getPerson() == null) {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                                res = new PersonResult("Error: Requested person does not exist.");
                            }
                            if (!res.getPerson().getUsername().equals(username)) {
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                                res = new PersonResult("Error: Requested person does not match user.");
                            }
                        }
                        if (res.isSuccess()) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        }
                    }
                } else {
                    res = new PersonResult("Error: No Auth Token given.");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
            }
        } catch (Exception ex) {
            res = new PersonResult(ex.getMessage());
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

    private JSONObject generateJson(PersonResult r) {
        JSONObject rAttributes = new JSONObject();
        if (r.isSuccess()) {
            if (r.isArray()) {
                JSONArray array = new JSONArray();
                for (int i = 0; i < r.getPersons().length; i++) {
                    array.put(generatePersonJson(r.getPersons()[i]));
                }
                rAttributes.put("data", array);
            } else {
                rAttributes = generatePersonJson(r.getPerson());
            }
        } else {
            rAttributes.put("message", r.getMessage());
        }
        rAttributes.put("success", r.isSuccess());
        return rAttributes;
    }

    private JSONObject generatePersonJson(Person p) {
        JSONObject person = new JSONObject();
        person.put("associatedUsername", p.getUsername());
        person.put("personID", p.getID());
        person.put("firstName", p.getFirstname());
        person.put("lastName", p.getLastname());
        person.put("gender", p.getGender());
        person.put("fatherID", p.getFatherID());
        person.put("motherID", p.getMotherID());
        person.put("spouseID", p.getSpouseID());

        return person;
    }
}
