package com.tamerbarsbay.depothouston.presentation.view.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tamerbarsbay.depothouston.R;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupToolbar();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new SettingsFragment())
                    .commit();
        }
    }

    private void setupToolbar() {
        final Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    public static class SettingsFragment extends PreferenceFragment {

        private static final String PREF_START_SCREEN = "pref_start_screen";
        private static final String PREF_NEARBY_THRESHOLD = "pref_nearby_threshold";
        private static final String PREF_CHANGELOG = "pref_changelog";
        private static final String PREF_RATE_APP = "pref_rate_app";
        private static final String PREF_ABOUT = "pref_about";

        //TODO inject changelog items, version number

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);

            bindPreferenceSummaryToValue(findPreference(PREF_START_SCREEN));
            bindPreferenceSummaryToValue(findPreference(PREF_NEARBY_THRESHOLD));

            Preference ratePref = findPreference(PREF_RATE_APP);
            Preference aboutPref = findPreference(PREF_ABOUT);
            Preference changelogPref = findPreference(PREF_CHANGELOG);

            ratePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    rateApp();
                    return true;
                }
            });

            aboutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    showAboutDialog();
                    return true;
                }
            });

            changelogPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    showChangelog();
                    return true;
                }
            });
        }

        private void showAboutDialog() {
            View dLayout = getActivity().getLayoutInflater().inflate(R.layout.view_about, null);

            TextView contactText = (TextView)dLayout.findViewById(R.id.tv_about_dialog_email);
            contactText.setOnClickListener(devEmailClickListener);
            contactText.setPaintFlags(contactText.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

            TextView versionText = (TextView)dLayout.findViewById(R.id.tv_about_dialog_version);
            String version = getVersionName(getActivity());
            if ("".equals(version)) {
                versionText.setVisibility(View.GONE);
            } else {
                versionText.setVisibility(View.VISIBLE);
                versionText.setText("v" + version);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(dLayout);
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        private void showChangelog() {
            View dLayout = getActivity().getLayoutInflater().inflate(R.layout.view_changelog, null);
            Button btnOkay = (Button)dLayout.findViewById(R.id.btn_changelog_okay);

            //TODO populate recyclerview

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(dLayout);
            final AlertDialog dialog = builder.create();
            dialog.show();

            btnOkay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        private String getVersionName(Context context) {
            String versionName = "";
            try {
                versionName = context.getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return versionName;
        }

        private void rateApp() {
            Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
            }
        }

        private View.OnClickListener devEmailClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent Email = new Intent(Intent.ACTION_SEND);
                Email.setType("text/email");
                Email.putExtra(Intent.EXTRA_EMAIL, new String[] { getString(R.string.contact_email) });
                startActivity(Intent.createChooser(Email, getString(R.string.contact_the_developer_of_depot)));
            }
        };
    }
}
