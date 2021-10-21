package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.familymapclient.Model.DataCache;

public class MainActivity extends AppCompatActivity implements LoginListener {

    private FragmentManager fm;
    private LoginFragment loginFragment;
    private MapFragment mapFragment;
    private DataCache dataCache;

    private boolean mLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.fm = getSupportFragmentManager();
        Fragment fragment = (Fragment) fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = createLoginFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    private LoginFragment createLoginFragment() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    private MapFragment createMapFragment() {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putBoolean(MapFragment.IS_EVENT, false);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onLogin() {
        mLogin = true;
        mapFragment = createMapFragment();
        fm.beginTransaction()
                .replace(R.id.fragmentContainer, mapFragment)
                .commit();
    }

    @Override
    public void onLogout() {
        mLogin = false;
        mapFragment = null;
        loginFragment = createLoginFragment();
        fm.beginTransaction()
                .replace(R.id.fragmentContainer, loginFragment)
                .commit();
    }

    @Override
    public void onRefresh() {
        mLogin = true;
        mapFragment = createMapFragment();
        fm.beginTransaction()
                .replace(R.id.fragmentContainer, mapFragment)
                .commit();
    }
}
