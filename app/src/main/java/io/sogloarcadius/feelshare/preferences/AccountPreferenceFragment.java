package io.sogloarcadius.feelshare.preferences;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;


import io.sogloarcadius.feelshare.R;

public class AccountPreferenceFragment extends PreferenceFragmentCompat {

        private static final String TAG = "AccountPreference";

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

            setPreferencesFromResource(R.xml.account_preference, rootKey);

            String authenticatedSummary = getString(R.string.account_already_login, FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

            Preference logoutPreference = findPreference("logout_account");

            logoutPreference.setSummaryProvider(new Preference.SummaryProvider() {
                @Override
                public CharSequence provideSummary(Preference preference) {
                    return authenticatedSummary;
                }
            });

            logoutPreference.setOnPreferenceClickListener(preference -> {

                //Ask the user if they want to logout
                new AlertDialog.Builder(getActivity())
                        .setIcon(R.drawable.ic_exit_to_app_black_24dp)
                        .setTitle(R.string.account_logout_button)
                        .setMessage(R.string.account_confirm_logout)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AuthUI.getInstance().signOut(getActivity());
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
                return true;
            });
        }
}
