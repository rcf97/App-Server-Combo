package com.example.familymapclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.familymapclient.Model.DataCache;

public class SettingsActivity extends AppCompatActivity {
    public static final int RESULT_LOGOUT = 5;
    public static final int RESULT_NONE = 6;

    DataCache dataCache;
    private boolean isChanged;

    private Switch mLifeStoryLinesSwitch;
    private Switch mFamilyTreeLinesSwitch;
    private Switch mSpouseLinesSwitch;
    private Switch mFathersSideSwitch;
    private Switch mMothersSideSwitch;
    private Switch mFilterEventsMaleSwitch;
    private Switch mFilterEventsFemaleSwitch;
    private RelativeLayout mLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dataCache = DataCache.getInstance();
        isChanged = false;

        mLifeStoryLinesSwitch = (Switch) findViewById(R.id.switchLifeStoryLines);
        mLifeStoryLinesSwitch.setChecked(dataCache.isLifeStoryLines());
        mFamilyTreeLinesSwitch = (Switch) findViewById(R.id.switchFamilyTreeLines);
        mFamilyTreeLinesSwitch.setChecked(dataCache.isFamilyTreeLines());
        mSpouseLinesSwitch = (Switch) findViewById(R.id.switchSpouseLines);
        mSpouseLinesSwitch.setChecked(dataCache.isSpouseLines());
        mFathersSideSwitch = (Switch) findViewById(R.id.switchFathersSide);
        mFathersSideSwitch.setChecked(dataCache.isFathersSide());
        mMothersSideSwitch = (Switch) findViewById(R.id.switchMothersSide);
        mMothersSideSwitch.setChecked(dataCache.isMothersSide());
        mFilterEventsMaleSwitch = (Switch) findViewById(R.id.switchFilterEventsMale);
        mFilterEventsMaleSwitch.setChecked(dataCache.isFilterMale());
        mFilterEventsFemaleSwitch = (Switch) findViewById(R.id.switchFilterEventsFemale);
        mFilterEventsFemaleSwitch.setChecked(dataCache.isFilterFemale());
        mLogout = (RelativeLayout) findViewById(R.id.Logout);
        mLogout.setClickable(true);

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });

        mLifeStoryLinesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataCache.setLifeStoryLines(isChecked);
                isChanged = true;
            }
        });
        mFamilyTreeLinesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataCache.setFamilyTreeLines(isChecked);
                isChanged = true;
            }
        });
        mSpouseLinesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataCache.setSpouseLines(isChecked);
                isChanged = true;
            }
        });
        mFathersSideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataCache.setFathersSide(isChecked);
                isChanged = true;
            }
        });
        mMothersSideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataCache.setMothersSide(isChecked);
                isChanged = true;
            }
        });
        mFilterEventsMaleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataCache.setFilterMale(isChecked);
                isChanged = true;
            }
        });
        mFilterEventsFemaleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataCache.setFilterFemale(isChecked);
                isChanged = true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (isChanged) {
                setResult(RESULT_OK);
            } else {
                setResult(RESULT_NONE);
            }
            finish();
        }
        return true;
    }

    private void Logout() {
        DataCache.getInstance().clear();
        Toast.makeText(this,
                "You are now logged out.",
                Toast.LENGTH_LONG).show();
        setResult(RESULT_LOGOUT);
        finish();
    }
}
