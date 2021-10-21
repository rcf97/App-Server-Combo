package Handler;

import DAO.DataAccessException;
import Request.RegisterRequest;
import Result.ClearResult;
import Result.RegisterResult;
import Service.ClearService;
import Service.RegisterService;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class ClearHandler extends RequestHandler implements HttpHandler  {
    @Override
    public void handle(HttpExchange exchange) {
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                ClearService clearService = new ClearService();
                ClearResult resData = clearService.clear();

                OutputStream respBody = exchange.getResponseBody();
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                writeString(generateJson(resData).toString(), respBody);
                respBody.close();
            }
        } catch (DataAccessException | IOException ex) {

        }
    }

    private JSONObject generateJson(ClearResult r) {
        JSONObject rAttributes = new JSONObject();
        rAttributes.put("message", r.getMessage());
        rAttributes.put("success", r.isSuccess());
        return rAttributes;
    }
}
