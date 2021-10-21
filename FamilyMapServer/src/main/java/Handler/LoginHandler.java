package Handler;

import DAO.DataAccessException;
import DAO.IncorrectPasswordException;
import DAO.UserDoesNotExistException;
import Request.LoginRequest;
import Request.RegisterRequest;
import Result.LoginResult;
import Result.RegisterResult;
import Service.LoginService;
import Service.RegisterService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class LoginHandler extends RequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        LoginResult resData = null;
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);
                LoginService loginService = new LoginService();
                LoginRequest request = this.parse(reqData);
                resData = loginService.login(request);

                OutputStream respBody = exchange.getResponseBody();

                if (!resData.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
                writeString(generateJson(resData).toString(), respBody);
                respBody.close();
            }
        } catch (Exception ex) {
        }
    }

    private LoginRequest parse(String jsonBody) {
        Gson gson = new Gson();
        return gson.fromJson(jsonBody, LoginRequest.class);
    }

    private JSONObject generateJson(LoginResult r) {
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
