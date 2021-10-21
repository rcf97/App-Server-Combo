package Handler;

import DAO.DataAccessException;
import Request.RegisterRequest;
import Result.RegisterResult;
import Service.RegisterService;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

public class RegisterHandler extends RequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);
                RegisterService registerService = new RegisterService();
                RegisterRequest request = this.parse(reqData);
                RegisterResult resData = registerService.register(request);

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

    private RegisterRequest parse(String jsonBody) {
        Gson gson = new Gson();
        return gson.fromJson(jsonBody, RegisterRequest.class);
    }

    private JSONObject generateJson(RegisterResult r) {
        JSONObject rAttributes = new JSONObject();
        if (r.isSuccess()) {
            rAttributes.put("authToken", r.getAuthToken());
            rAttributes.put("userName", r.getUserName());
            rAttributes.put("personID", r.getPersonID());
        } else {
            rAttributes.put("message", r.getMessage());
        }
        rAttributes.put("success", r.isSuccess());
        return rAttributes;
    }
}
