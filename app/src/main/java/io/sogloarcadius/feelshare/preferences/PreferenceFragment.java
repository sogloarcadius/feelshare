package io.sogloarcadius.feelshare.preferences;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import io.sogloarcadius.feelshare.R;

public class PreferenceFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
        {
            setPreferencesFromResource(R.xml.preferences, rootKey);
        }


}
