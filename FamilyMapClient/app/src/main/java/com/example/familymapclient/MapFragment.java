package com.example.familymapclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.familymapclient.Model.DataCache;
import com.example.familymapclient.Model.EventExt;
import com.example.familymapclient.Model.PersonExt;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.*;

public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLoadedCallback {

    public static final String IS_EVENT = "isEvent";
    public static final String EVENT_ID = "Event_ID";

    private LoginListener mLoginListener;
    private boolean isEvent;
    private String eventID;

    private GoogleMap mMap;
    private List<Polyline> polylines;

    private ImageView mImageView;
    private TextView mTextView;
    private LinearLayout mMapTextView;

    private EventExt curEvent;

    private DataCache dataCache;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataCache = DataCache.getInstance();
        this.isEvent = this.getArguments().getBoolean(IS_EVENT);
        if (isEvent) {
            this.eventID = this.getArguments().getString(EVENT_ID);
            this.curEvent = dataCache.getEvent(this.eventID);
        }
        polylines = new ArrayList<>();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        Iconify.with(new FontAwesomeModule());

        mImageView = (ImageView) view.findViewById(R.id.genderIcon);
        mTextView = (TextView) view.findViewById(R.id.eventInfo);
        mMapTextView = (LinearLayout) view.findViewById(R.id.mapTextView);
        mMapTextView.setClickable(true);
        mMapTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPersonActivity();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (!this.isEvent) {
            inflater.inflate(R.menu.map_menu, menu);

            MenuItem searchMenuItem = menu.findItem(R.id.searchMenuItem);
            searchMenuItem.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_search)
                    .colorRes(R.color.colorWhite)
                    .actionBarSize());

            MenuItem settingsMenuItem = menu.findItem(R.id.settingsMenuItem);
            settingsMenuItem.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_gear)
                    .colorRes(R.color.colorWhite)
                    .actionBarSize());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(this);
        Map<String, EventExt> events = dataCache.getEvents();
        Map<String, Float> markerColors;
        if (events.size() == 0) {
            return;
        }
        if (dataCache.getMarkerColors() == null) {
            markerColors = this.assignColors();
            dataCache.setMarkerColors(markerColors);
        } else {
            markerColors = dataCache.getMarkerColors();
        }

        for (String id : events.keySet()) {
            EventExt event = events.get(id);
            LatLng location = new LatLng(event.getLatitude(), event.getLongitude());
            float hue = markerColors.get(event.getType().toLowerCase());
            Marker newMarker = mMap.addMarker(new MarkerOptions()
                    .position(location)
                    .icon(defaultMarker(hue)));
            newMarker.setTag(event);
            if (!this.isEvent) {
                if (event.getPerson_ID().equals(dataCache.getUser().getID()) &&
                        event.getType().toUpperCase().equals("BIRTH")) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                    this.updateInfoWindow(event);
                    this.curEvent = event;
                }
            }
        }
        drawLines();
        if (this.isEvent) {
            LatLng location = new LatLng(this.curEvent.getLatitude(), this.curEvent.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            this.updateInfoWindow(this.curEvent);
        }

        mMap.setOnMarkerClickListener(this);
    }

    private void drawLines() {
        if (this.curEvent == null) {
            return;
        }
        PersonExt person = this.curEvent.getPerson();
        LatLng centerEvent = new LatLng(this.curEvent.getLatitude(), this.curEvent.getLongitude());
        String gender = person.getGender();
        String spouseGender;
        if (gender.toLowerCase().equals("m")) {
            spouseGender = "f";
        } else {
            spouseGender = "m";
        }
        if (dataCache.isSpouseLines() &&
                person.getSpouse() != null &&
                person.getSpouse().getEvents().size() > 0) {
            if ((spouseGender.equals("m") && dataCache.isFilterMale()) ||
                    (spouseGender.equals("f") && dataCache.isFilterFemale())) {
                EventExt spouseEvent = person.getSpouse().getEvents().get(0);
                LatLng spouseLocation = new LatLng(spouseEvent.getLatitude(), spouseEvent.getLongitude());
                Polyline line = this.mMap.addPolyline(new PolylineOptions()
                        .add(centerEvent, spouseLocation)
                        .color(Color.RED));
                polylines.add(line);
            }
        }
        if (dataCache.isLifeStoryLines()) {
            List<EventExt> events = person.getEvents();
            if (events.size() > 1) {
                for (int i = 0; i < events.size() - 1; i++) {
                    EventExt first = events.get(i);
                    EventExt second = events.get(i + 1);
                    LatLng firstLoc = new LatLng(first.getLatitude(), first.getLongitude());
                    LatLng secondLoc = new LatLng(second.getLatitude(), second.getLongitude());
                    Polyline line = this.mMap.addPolyline(new PolylineOptions()
                            .add(firstLoc, secondLoc)
                            .color(Color.YELLOW));
                    polylines.add(line);
                }
            }
        }
        if (dataCache.isFamilyTreeLines()) {
            if (person.getFather() != null && dataCache.isFilterMale() && dataCache.isFathersSide()) {
                drawFamilyTreeLines(person.getFather(), 10, centerEvent);
            }
            if (person.getMother() != null && dataCache.isFilterFemale() && dataCache.isMothersSide()) {
                drawFamilyTreeLines(person.getMother(), 10, centerEvent);
            }
        }
    }

    private void drawFamilyTreeLines(PersonExt person, float width, LatLng prevLoc) {
        if (person.getEvents().size() > 0) {
            EventExt location = person.getEvents().get(0);
            LatLng newLoc = new LatLng(location.getLatitude(), location.getLongitude());
            Polyline line = this.mMap.addPolyline(new PolylineOptions()
                    .add(prevLoc, newLoc)
                    .width(width)
                    .color(Color.BLUE));
            polylines.add(line);
            if (person.getFather() != null && dataCache.isFilterMale()) {
                drawFamilyTreeLines(person.getFather(), width / 2, newLoc);
            }
            if (person.getMother() != null && dataCache.isFilterFemale()) {
                drawFamilyTreeLines(person.getMother(), width / 2, newLoc);
            }
        }

    }

    private Map<String, Float> assignColors() {
        Set<String> types = dataCache.getEventTypes();
        Map<String, Float> markerColors = new HashMap<>();
        Float[] hues = { HUE_AZURE, HUE_BLUE, HUE_CYAN, HUE_ORANGE, HUE_ROSE,
                HUE_GREEN, HUE_MAGENTA, HUE_RED, HUE_VIOLET, HUE_YELLOW };
        Set<Integer> used = new HashSet<>();
        Random random = new Random();

        for (String type : types) {
            if (used.size() == 10) {
                used.clear();
            }
            int i;
            do {
                i = random.nextInt() % 10;
                if (i < 0) {
                    i *= -1;
                }
            } while (used.contains(i));
            markerColors.put(type.toLowerCase(), hues[i]);
            used.add(i);
        }
        return markerColors;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        EventExt event = (EventExt) marker.getTag();
        this.updateInfoWindow(event);
        return false;
    }

    private void updateInfoWindow(EventExt event) {
        PersonExt person = dataCache.getPerson(event.getPerson_ID());

        if (person.getGender().equals("m")) {
            mImageView.setImageResource(R.drawable.male);
        } else {
            mImageView.setImageResource(R.drawable.female);
        }

        String text = person.getFirstname() + " " + person.getLastname() + "\n" +
                event.getType() + ": " + event.getCity() + ", " + event.getCountry() +
                " (" + event.getYear() + ")";

        mTextView.setText(text);

        this.curEvent = event;
        for (Polyline line : polylines) {
            line.remove();
        }
        polylines.clear();
        drawLines();
    }

    @Override
    public void onMapLoaded() {

    }

    private void startPersonActivity() {
        Intent intent = new Intent(getActivity(), PersonActivity.class);
        intent.putExtra(PersonActivity.PERSON, this.curEvent.getPerson_ID());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        Intent intent;
        switch(menu.getItemId()) {
            case R.id.searchMenuItem:
                intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                return true;
            case R.id.settingsMenuItem:
                intent = new Intent(getActivity(), SettingsActivity.class);
                startActivityForResult(intent, 1);
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == SettingsActivity.RESULT_LOGOUT) {
                mLoginListener.onLogout();
            } else if (resultCode == SettingsActivity.RESULT_OK) {
                mLoginListener.onRefresh();
            } else if (resultCode == SettingsActivity.RESULT_NONE) {
                return;
            }
        }
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
