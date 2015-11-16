package com.naman14.powermenu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.ListPreference;
import android.preference.Preference;
import android.widget.Toast;

import com.naman14.powermenu.xposed.XposedMainActivity;

import eu.chainfire.libsuperuser.Shell;

/**
 * Created by naman on 16/11/15.
 */
public class PreferenceFragment extends android.preference.PreferenceFragment {

    private Preference rootstatus, source, rate, share, shortcut, about;
    private ListPreference themePreference;

    private static final int BG_PRIO = android.os.Process.THREAD_PRIORITY_BACKGROUND;

    private String Urlgithub = "https://github.com/naman14/MaterialPowerMenu";
    private String Urlrate = "https://play.google.com/store/apps/details?id=com.naman14.powermenu";
    private String Urlapps = "https://play.google.com/store/apps/developer?id=Naman14";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.prefs);

        rootstatus = findPreference("root_status");
        themePreference = (ListPreference) findPreference("pref_theme");

        source = findPreference("source_github");
        source.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Urlgithub));
                startActivity(i);
                return true;
            }
        });

        rate = findPreference("rate_app");
        rate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Urlrate));
                startActivity(i);
                return true;
            }
        });

        share = findPreference("share_app");
        share.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Material Power Menu");
                String sAux = "\nCheck out this beautiful app to add rebooting functionality to your rooted android device\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=com.naman14.powermenu \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Choose one"));
                return true;
            }
        });

        shortcut = findPreference("pref_homescreen_shortcut");
        shortcut.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                addShortcut();
                return true;
            }
        });

        about = findPreference("pref_about_donate");
        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Urlapps));
                startActivity(i);
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                setThreadPrio(BG_PRIO);

                if (Shell.SU.available()) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (rootstatus != null) {
                                rootstatus.setTitle("Root available");

                                rootstatus.setSummary("Root is available. App should work fine");
                            }
                        }
                    });
                }
            }
        }).start();


        themePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Intent i = getActivity().getBaseContext().getPackageManager().getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return true;
            }
        });
    }

    private static void setThreadPrio(int prio) {
        android.os.Process.setThreadPriority(prio);
    }

    private void addShortcut() {
        //Adding shortcut for MainActivity
        //on Home screen
        Intent shortcutIntent = new Intent(getActivity(),
                XposedMainActivity.class);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Power Menu");
        addIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        addIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getActivity(),
                        R.drawable.ic_reboot));
        addIntent
                .setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getActivity().getApplicationContext().sendBroadcast(addIntent);
        Toast.makeText(getActivity().getApplicationContext(), "Added Home Screen Shortcurt", Toast.LENGTH_SHORT).show();
        getActivity().finish();

    }
}
