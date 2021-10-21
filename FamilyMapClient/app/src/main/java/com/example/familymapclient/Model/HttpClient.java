package com.example.familymapclient.Model;

import android.util.Log;

import com.example.familymapclient.Model.EventJsonObject;
import com.example.familymapclient.Model.EventsJsonObject;
import com.example.familymapclient.Model.PersonJsonObject;
import com.example.familymapclient.Model.PersonsJsonObject;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import Model.Event;
import Model.Person;
import Request.LoginRequest;
import Request.RegisterRequest;
import Result.EventResult;
import Result.LoginResult;
import Result.PersonResult;
import Result.RegisterResult;

public class HttpClient {

    private static final String LOG_TAG = "HttpClient";

    //Login functionality
    public LoginResult sendRequest(URL url, LoginRequest r) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            writeString(generateJson(r).toString(), connection.getOutputStream());

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = connection.getInputStream();
                String resData = readString(responseBody);
                LoginResult loginResult = this.parse(resData, r);
                return loginResult;
            } else if (connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                InputStream responseBody = connection.getErrorStream();
                String resData = readString(responseBody);
                LoginResult loginResult = this.parse(resData, r);
                return loginResult;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return null;
    }

    private JSONObject generateJson(LoginRequest r) {

        try {
            JSONObject rAttributes = new JSONObject()
                    .put("userName", r.getUserName())
                    .put("password", r.getPassword());
            return rAttributes;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return null;
    }

    private LoginResult parse(String jsonBody, LoginRequest r) {
        Gson gson = new Gson();
        return gson.fromJson(jsonBody, LoginResult.class);
    }

    //Register Functionality
    public RegisterResult sendRequest(URL url, RegisterRequest r) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            writeString(generateJson(r).toString(), connection.getOutputStream());

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = connection.getInputStream();
                String resData = readString(responseBody);
                RegisterResult registerResult = this.parse(resData, r);
                return registerResult;
            } else if (connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                InputStream responseBody = connection.getErrorStream();
                String resData = readString(responseBody);
                RegisterResult registerResult = this.parse(resData, r);
                return registerResult;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return null;
    }

    private JSONObject generateJson(RegisterRequest r) {
        JSONObject rAttributes = new JSONObject();
        try {
            rAttributes.put("userName", r.getUserName());
            rAttributes.put("password", r.getPassword());
            rAttributes.put("email", r.getEmail());
            rAttributes.put("firstName", r.getFirstName());
            rAttributes.put("lastName", r.getLastName());
            rAttributes.put("gender", r.getGender());
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return rAttributes;
    }

    private RegisterResult parse(String jsonBody, RegisterRequest r) {
        Gson gson = new Gson();
        return gson.fromJson(jsonBody, RegisterResult.class);
    }

    //Get Persons Functionality
    public PersonResult getPersons(URL url, String authToken) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Authorization", authToken);
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = connection.getInputStream();
                String resData = readString(responseBody);
                return parsePersons(resData);
            } else if (connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                InputStream responseBody = connection.getErrorStream();
                String resData = readString(responseBody);
                return parsePersons(resData);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return null;
    }

    private PersonResult parsePersons(String jsonBody) {
        Gson gson = new Gson();
        if (jsonBody.contains("data")) {
            PersonsJsonObject obj = gson.fromJson(jsonBody, PersonsJsonObject.class);
            return new PersonResult(obj.getData());
        } else if (jsonBody.contains("Invalid Auth Token")) {
            PersonsJsonObject obj = gson.fromJson(jsonBody, PersonsJsonObject.class);
            return new PersonResult(obj.getMessage());
        } else {
            PersonJsonObject obj = gson.fromJson(jsonBody, PersonJsonObject.class);
            return new PersonResult(new Person(obj.getID(), obj.getUsername(), obj.getFirstname(),
                    obj.getLastname(), obj.getGender(), obj.getFatherID(), obj.getMotherID(),
                    obj.getSpouseID()));
        }
    }

    //Get Event Functionality
    public EventResult getEvents(URL url, String authToken) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Authorization", authToken);
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = connection.getInputStream();
                String resData = readString(responseBody);
                return parseEvents(resData);
            } else if (connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                InputStream responseBody = connection.getErrorStream();
                String resData = readString(responseBody);
                return parseEvents(resData);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return null;
    }

    private EventResult parseEvents(String jsonBody) {
        Gson gson = new Gson();
        if (jsonBody.contains("data")) {
            EventsJsonObject obj = gson.fromJson(jsonBody, EventsJsonObject.class);
            return new EventResult(obj.getData());
        } else if (jsonBody.contains("Invalid Auth Token")) {
            EventsJsonObject obj = gson.fromJson(jsonBody, EventsJsonObject.class);
            return new EventResult(obj.getMessage());
        }else {
            EventJsonObject obj = gson.fromJson(jsonBody, EventJsonObject.class);
            return new EventResult(new Event(obj.getID(), obj.getUser(), obj.getPerson_ID(),
                    obj.getLatitude(), obj.getLongitude(), obj.getCountry(), obj.getCity(),
                    obj.getType(), obj.getYear()));
        }
    }

    //General functions
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(sw);
        bw.write(str);
        bw.flush();
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

}
