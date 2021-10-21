package Handler;

import DAO.DataAccessException;
import Service.FillService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import Result.FillResult;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class FillHandler extends RequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        FillResult res = null;
        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                String url = exchange.getRequestURI().toString();
                url = url.substring(6);
                Scanner scanner = new Scanner(url);
                scanner.useDelimiter("/");
                String userName = scanner.next();
                int generation = 4;
                if (scanner.hasNext()) {
                    generation = scanner.nextInt();
                }
                if (generation < 0) {
                    throw new InputMismatchException();
                }
                FillService fillService = new FillService();
                res = fillService.fill(userName, generation);
            }
        } catch (InputMismatchException ex) {
            res = new FillResult ("Invalid generations parameter.", false);
        } catch (NoSuchElementException ex) {
            res = new FillResult("Invalid username parameter.", false);
        } catch (DataAccessException ex) {
            res = new FillResult(ex.getMessage(), false);
        }

        OutputStream respBody = exchange.getResponseBody();
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

        writeString(generateJson(res).toString(), respBody);
        respBody.close();
    }

    private JSONObject generateJson(FillResult r) {
        JSONObject rAttributes = new JSONObject();
        rAttributes.put("message", r.getMessage());
        rAttributes.put("success", r.isSuccess());
        return rAttributes;
    }
}
