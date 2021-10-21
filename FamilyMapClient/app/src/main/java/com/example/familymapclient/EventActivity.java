package com.example.familymapclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.familymapclient.Model.DataCache;
import com.example.familymapclient.Model.EventExt;

public class EventActivity extends AppCompatActivity implements LoginListener {
    public static final String EVENT_ID = "EventID";

    private EventExt event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.event = DataCache.getInstance().getEvent(this.getIntent().getStringExtra(EVENT_ID));
        setContentView(R.layout.activity_event);

        MapFragment mapFragment;
        FragmentManager fm = getSupportFragmentManager();
        mapFragment = (MapFragment) fm.findFragmentById(R.id.mapFragment);
        if (mapFragment == null) {
            mapFragment = new MapFragment();
            Bundle args = new Bundle();
            args.putBoolean(MapFragment.IS_EVENT, true);
            args.putString(MapFragment.EVENT_ID, event.getID());
            mapFragment.setArguments(args);
            fm.beginTransaction()
                    .add(R.id.mapFragment, mapFragment)
                    .commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onLogin() {
        return;
    }

    @Override
    public void onLogout() {
        return;
    }

    @Override
    public void onRefresh() {
        return;
    }
}
