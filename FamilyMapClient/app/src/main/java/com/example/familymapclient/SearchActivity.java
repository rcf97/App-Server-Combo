package com.example.familymapclient;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familymapclient.Model.DataCache;
import com.example.familymapclient.Model.EventExt;
import com.example.familymapclient.Model.PersonExt;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private static final int PERSON_VIEW_TYPE = 0;
    private static final int EVENT_VIEW_TYPE = 1;

    private DataCache dataCache;
    private RecyclerView searchResults;
    private EditText searchBox;
    private SearchResultAdapter searchResultAdapter;
    private List<PersonExt> persons;
    private List<EventExt> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchBox = findViewById(R.id.SearchText);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                persons.clear();
                events.clear();
                persons.addAll(dataCache.searchPersons(searchBox.getText().toString()));
                events.addAll(dataCache.searchEvents(searchBox.getText().toString()));
                searchResultAdapter.notifyDataSetChanged();
            }
        });


        searchResults = findViewById(R.id.SearchResults);
        searchResults.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        dataCache = DataCache.getInstance();
        persons = new ArrayList<>();
        events = new ArrayList<>();

        searchResultAdapter = new SearchResultAdapter(persons, events);
        searchResults.setAdapter(searchResultAdapter);
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

    private class SearchResultAdapter extends RecyclerView.Adapter<SearchResultViewHolder> {
        private final List<PersonExt> personResults;
        private final List<EventExt> eventResults;

        SearchResultAdapter(List<PersonExt> personResults, List<EventExt> eventResults) {
            this.personResults = personResults;
            this.eventResults = eventResults;
        }

        @Override
        public int getItemViewType(int position) {
            return position < personResults.size() ? PERSON_VIEW_TYPE : EVENT_VIEW_TYPE;
        }

        @NonNull
        @Override
        public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getBaseContext());
            View view = layoutInflater.inflate(R.layout.person_item, parent, false);
            return new SearchResultViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
            if (position < personResults.size()) {
                holder.bind(personResults.get(position));
            } else {
                holder.bind(eventResults.get(position - personResults.size()));
            }
        }

        @Override
        public int getItemCount() {
            return personResults.size() + eventResults.size();
        }
    }

    private class SearchResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView icon;
        private final TextView first;
        private final TextView second;

        private final int viewType;
        private PersonExt person;
        private EventExt event;

        public SearchResultViewHolder(@NonNull View view, int viewType) {
            super(view);

            this.viewType = viewType;

            icon = itemView.findViewById(R.id.icon);
            first = itemView.findViewById(R.id.first);
            second = itemView.findViewById(R.id.second);

            itemView.setOnClickListener(this);
        }

        private void bind(PersonExt person) {
            this.person = person;
            if (person.getGender().toLowerCase().equals("m")) {
                icon.setImageResource(R.drawable.male);
            } else {
                icon.setImageResource(R.drawable.female);
            }

            String str = person.getFirstname() + " " + person.getLastname();
            first.setText(str);

            second.setText("");
        }

        private void bind(EventExt event) {
            this.event = event;
            icon.setImageDrawable(new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker)
                    .colorRes(R.color.markerRed).sizeDp(40));

            String str = event.getType() + ": " + event.getCity() + ", " +
                    event.getCountry() + " (" + event.getYear() + ")";
            first.setText(str);

            str = event.getPerson().getFirstname() + " " + event.getPerson().getLastname();
            second.setText(str);
        }

        @Override
        public void onClick(View v) {
            if (viewType == PERSON_VIEW_TYPE) {
                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                intent.putExtra(PersonActivity.PERSON, person.getID());
                startActivity(intent);
            } else {
                Intent intent = new Intent(SearchActivity.this, EventActivity.class);
                intent.putExtra(EventActivity.EVENT_ID, event.getID());
                startActivity(intent);
            }
        }
    }
}
