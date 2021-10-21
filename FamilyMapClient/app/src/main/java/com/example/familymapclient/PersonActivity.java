package com.example.familymapclient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.familymapclient.Model.DataCache;
import com.example.familymapclient.Model.EventExt;
import com.example.familymapclient.Model.PersonExt;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PersonActivity extends AppCompatActivity {
    public static final String PERSON = "Person";

    private DataCache dataCache;

    private PersonExt person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        dataCache = DataCache.getInstance();
        String id = getIntent().getStringExtra(PERSON);
        this.person = dataCache.getPerson(id);

        TextView mFirstNameLine = (TextView) findViewById(R.id.firstNameLine);
        mFirstNameLine.setText(this.person.getFirstname());

        TextView mLastNameLine = (TextView) findViewById(R.id.lastNameLine);
        mLastNameLine.setText(this.person.getLastname());

        TextView mGenderLine = (TextView) findViewById(R.id.genderLine);
        if (this.person.getGender().equals("m")) {
            mGenderLine.setText("Male");
        } else {
            mGenderLine.setText("Female");
        }


        List<EventExt> eventList = this.person.getEvents();
        Map<String, EventExt> allEvents = dataCache.getEvents();
        for (int i = 0; i < eventList.size(); i++) {
            if (!allEvents.keySet().contains(eventList.get(i).getID())) {
                eventList.remove(i);
                i--;
            }
        }

        List<Pair<PersonExt, String>> familyList = new ArrayList<>();
        if (person.getFather() != null) {
            familyList.add(new Pair<>(person.getFather(), "Father"));
        }
        if (person.getMother() != null) {
            familyList.add(new Pair<>(person.getMother(), "Mother"));
        }
        if (person.getSpouse() != null) {
            familyList.add(new Pair<>(person.getSpouse(), "Spouse"));
        }
        if (person.getChild() != null) {
            familyList.add(new Pair<>(person.getChild(), "Child"));
        }

        ExpandableListView expandableListView = findViewById(R.id.expandableList);

        expandableListView.setAdapter(new ExpandableListAdapter(eventList, familyList));
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

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private static final int LIFE_EVENTS_GROUP_POSITION = 0;
        private static final int FAMILY_GROUP_POSITION = 1;

        private final List<EventExt> lifeEvents;
        private final List<Pair<PersonExt, String>> family;

        ExpandableListAdapter(List<EventExt> lifeEvents, List<Pair<PersonExt, String>> family) {
            this.lifeEvents = lifeEvents;
            this.family = family;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    return lifeEvents.size();
                case FAMILY_GROUP_POSITION:
                    return family.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    return R.string.lifeEvents;
                case FAMILY_GROUP_POSITION:
                    return R.string.family;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    return lifeEvents.get(childPosition);
                case FAMILY_GROUP_POSITION:
                    return family.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.person_group_header, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.groupName);

            switch (groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    titleView.setText(R.string.lifeEvents);
                    break;
                case FAMILY_GROUP_POSITION:
                    titleView.setText(R.string.family);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView = getLayoutInflater().inflate(R.layout.person_item, parent, false);
            initializeItemView(groupPosition, itemView, childPosition);
            return itemView;
        }

        private void initializeItemView(int groupPosition, View itemView, int childPosition) {
            ImageView icon = itemView.findViewById(R.id.icon);
            TextView first = itemView.findViewById(R.id.first);
            TextView second = itemView.findViewById(R.id.second);
            String str;
            switch(groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    EventExt event = lifeEvents.get(childPosition);

                    icon.setImageDrawable(new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker)
                            .colorRes(R.color.markerRed).sizeDp(40));

                    str = event.getType() + ": " + event.getCity() + ", " +
                            event.getCountry() + " (" + event.getYear() + ")";
                    first.setText(str);

                    str = event.getPerson().getFirstname() + " " + event.getPerson().getLastname();
                    second.setText(str);

                    itemView.setTag(event);

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onEventClick(v);
                        }
                    });
                    break;
                case FAMILY_GROUP_POSITION:
                    Pair<PersonExt, String> person = family.get(childPosition);

                    if (person.first.getGender().toUpperCase().equals("M")) {
                        icon.setImageResource(R.drawable.male);
                    } else {
                        icon.setImageResource(R.drawable.female);
                    }

                    str = person.first.getFirstname() + " " + person.first.getLastname();
                    first.setText(str);

                    str = person.second;
                    second.setText(str);

                    itemView.setTag(person.first);

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onPersonClick(v);
                        }
                    });
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        private void onEventClick(View v) {
            EventExt event = (EventExt) v.getTag();
            Intent intent = new Intent(PersonActivity.this, EventActivity.class);
            intent.putExtra(EventActivity.EVENT_ID, event.getID());
            startActivity(intent);
        }

        private void onPersonClick(View v) {
            PersonExt person = (PersonExt) v.getTag();
            Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
            intent.putExtra(PersonActivity.PERSON, person.getID());
            startActivity(intent);
        }
    }
}
