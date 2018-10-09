package com.example.newsfeedapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by Ferhat on 12.5.2018.
 */

public class NewsSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_settings);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    public static class NewsSettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        private static final String search = "search";

        private static final String orderBy = "orderBy";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.setting_search);
            addPreferencesFromResource(R.xml.setting_order);

            SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();

            EditTextPreference editTextPreference = (EditTextPreference) findPreference(search);
            editTextPreference.setSummary(sharedPreferences.getString(search, ""));

            ListPreference listPreference = (ListPreference) findPreference(orderBy);
            listPreference.setSummary(listPreference.getEntry());

        }

        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key){

            Preference preference = findPreference(key);

            if (preference instanceof EditTextPreference) {
                EditTextPreference editText = (EditTextPreference) preference;
                preference.setSummary(editText.getText());
            }

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                preference.setSummary(listPreference.getEntry());
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                Intent news = new Intent(NewsSettings.this,NewsActivity.class);
                startActivity(news);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
