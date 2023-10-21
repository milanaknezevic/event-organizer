package com.example.eventorganizer.ui;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.eventorganizer.R;

import java.util.Locale;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        findPreference(getString(R.string.language_key)).setOnPreferenceChangeListener(this);
        findPreference(getString(R.string.notification_key)).setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Context context = requireContext();
        requireActivity().finish();
        startActivity(requireActivity().getIntent());
        return true;
    }

    private void setLanguage(String language, Context context) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
       String language = sharedPreferences.getString(
                "language",
                context.getResources().getString(R.string.default_language)
        );
       setLanguage(language, context);
    }
}