package com.example.familymapclient;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.familymapclient.Model.DataCache;
import com.example.familymapclient.Model.FamilyRequest;
import com.example.familymapclient.Model.HttpClient;

import java.net.MalformedURLException;
import java.net.URL;

import Model.Event;
import Model.Person;
import Request.LoginRequest;
import Request.RegisterRequest;
import Request.Request;
import Result.*;

public class LoginFragment extends Fragment {
    private static final String LOG_TAG = "LoginFragment";
    private String path = "http://";
    LoginListener mLoginListener;

    private Button mLoginButton;
    private Button mRegisterButton;

    private EditText mServerHostField;
    private EditText mServerPortField;
    private EditText mUsername;
    private EditText mPassword;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;

    private RadioGroup mGender;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            enableLoginIfReady();
            enableRegisterIfReady();
        }
    };

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mLoginButton = (Button) view.findViewById(R.id.loginButton);
        mRegisterButton = (Button) view.findViewById(R.id.registerButton);

        mServerHostField = (EditText) view.findViewById(R.id.serverHostField);
        mServerPortField = (EditText) view.findViewById(R.id.serverPortField);
        mUsername = (EditText) view.findViewById(R.id.userNameField);
        mPassword = (EditText) view.findViewById(R.id.passwordField);
        mFirstName = (EditText) view.findViewById(R.id.firstNameField);
        mLastName = (EditText) view.findViewById(R.id.lastNameField);
        mEmail = (EditText) view.findViewById(R.id.emailField);

        mGender = (RadioGroup) view.findViewById(R.id.radioGender);

        mLoginButton.setEnabled(false);
        mRegisterButton.setEnabled(false);

        mServerHostField.addTextChangedListener(textWatcher);
        mServerPortField.addTextChangedListener(textWatcher);
        mUsername.addTextChangedListener(textWatcher);
        mPassword.addTextChangedListener(textWatcher);
        mFirstName.addTextChangedListener(textWatcher);
        mLastName.addTextChangedListener(textWatcher);
        mEmail.addTextChangedListener(textWatcher);
        mGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                enableRegisterIfReady();
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLogin();
            }
        });
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRegister();
            }
        });

        return view;
    }

    private void enableLoginIfReady() {
        if (mServerHostField.getText().toString().equals("") ||
                mServerPortField.getText().toString().equals("") ||
                mUsername.getText().toString().equals("") ||
                mPassword.getText().toString().equals("")) {
            mLoginButton.setEnabled(false);
        } else {
            mLoginButton.setEnabled(true);
        }
    }

    private void enableRegisterIfReady() {
        if (mServerHostField.getText().toString().equals("") ||
                mServerPortField.getText().toString().equals("") ||
                mUsername.getText().toString().equals("") ||
                mPassword.getText().toString().equals("") ||
                mFirstName.getText().toString().equals("") ||
                mLastName.getText().toString().equals("") ||
                mEmail.getText().toString().equals("") ||
                mGender.getCheckedRadioButtonId() == -1) {
            mRegisterButton.setEnabled(false);
        } else {
            mRegisterButton.setEnabled(true);
        }
    }

    private void handleLogin() {
        String loginPath = path + (mServerHostField.getText().toString() + ":" +
                mServerPortField.getText().toString());
        LoginRequest loginRequest = new LoginRequest(mUsername.getText().toString(),
                mPassword.getText().toString());
        try {
            Pair<URL, Request> pair = new Pair(new URL(loginPath + "/user/login"), loginRequest);
            WebAccessTask task = new WebAccessTask();
            task.execute(pair);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    private void handleRegister() {
        String registerPath = path + (mServerHostField.getText().toString() + ":" +
                mServerPortField.getText().toString());
        String gender;
        if (mGender.getCheckedRadioButtonId() == R.id.radioMale) {
            gender = "m";
        } else {
            gender = "f";
        }
        RegisterRequest registerRequest = new RegisterRequest(mUsername.getText().toString(),
                mPassword.getText().toString(), mEmail.getText().toString(),
                mFirstName.getText().toString(), mLastName.getText().toString(), gender);
        try {
            Pair<URL, Request> pair = new Pair(new URL(registerPath + "/user/register"), registerRequest);
            WebAccessTask task = new WebAccessTask();
            task.execute(pair);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    private class WebAccessTask extends AsyncTask<Pair<URL, Request>, Void, Result> {

        @Override
        protected Result doInBackground(Pair<URL, Request>... pairs) {
            HttpClient client = new HttpClient();
            Result res = null;

            if (pairs.length == 0) {
                return null;
            }
            if (pairs[0].second.getClass() == LoginRequest.class) {
                res = client.sendRequest(pairs[0].first, (LoginRequest) pairs[0].second);
            } else if (pairs[0].second.getClass() == RegisterRequest.class) {
                res = client.sendRequest(pairs[0].first, (RegisterRequest) pairs[0].second);
            } else if (pairs[0].second.getClass() == FamilyRequest.class) {
                FamilyRequest req = (FamilyRequest) pairs[0].second;
                if (req.getType() == Person.class) {
                    res = client.getPersons(pairs[0].first, req.getAuthToken());
                } else if (req.getType() == Event.class) {
                    res = client.getEvents(pairs[0].first, req.getAuthToken());
                }
            } else {
                return null;
            }
            return res;
        }

        @Override
        protected void onPostExecute(Result res) {
            try {
                DataCache dataCache = DataCache.getInstance();
                if (res.getClass() == RegisterResult.class || res.getClass() == LoginResult.class) {
                    if (!res.isSuccess()) {
                        Toast.makeText(getActivity(),
                                getString(R.string.failedToast) + "\n" + res.getMessage(),
                                Toast.LENGTH_LONG).show();
                    } else {
                        getData(res);
                    }
                } else if (res.getClass() == PersonResult.class) {
                    PersonResult personResult = (PersonResult) res;
                    if (personResult.isArray()) {
                        dataCache.loadData((PersonResult) res);
                    } else {
                        Person user = personResult.getPerson();
                        dataCache.setUser(user);
                        Toast.makeText(getActivity(),
                                "Welcome, " + user.getFirstname() + " " + user.getLastname(),
                                Toast.LENGTH_SHORT).show();
                        dataCache.organize();
                        LoginSuccess();
                    }
                } else if (res.getClass() == EventResult.class) {
                    dataCache.loadData((EventResult) res);
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        private void getData(Result res) {
            String personPath = path + (mServerHostField.getText().toString() + ":" +
                    mServerPortField.getText().toString()) + "/person";
            String eventPath = path + (mServerHostField.getText().toString() + ":" +
                    mServerPortField.getText().toString()) + "/event";
            String userID = ((LoginResult) res).getPersonID();
            String authToken = ((LoginResult) res).getAuthToken();
            Person user = null;
            String userPath = path + (mServerHostField.getText().toString() + ":" +
                    mServerPortField.getText().toString()) + "/person/" + userID;
            try {
                WebAccessTask task = new WebAccessTask();
                task.execute(new Pair(new URL(personPath), new FamilyRequest(authToken, Person.class)));
                WebAccessTask task1 = new WebAccessTask();
                task1.execute(new Pair(new URL(eventPath), new FamilyRequest(authToken, Event.class)));
                WebAccessTask task2 = new WebAccessTask();
                task2.execute(new Pair(new URL(userPath), new FamilyRequest(authToken, Person.class)));
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
            DataCache dataCache = DataCache.getInstance();
            dataCache.setAuthToken(authToken);
        }
    }

    private void LoginSuccess() {
        mLoginListener.onLogin();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginListener) {
            mLoginListener = (LoginListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + "must implement LoginListener");
        }
    }
}
