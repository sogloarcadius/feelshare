package io.sogloarcadius.feelshare.preference;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;

import io.sogloarcadius.feelshare.R;
import io.sogloarcadius.feelshare.model.SaveMood;
import io.sogloarcadius.feelshare.model.SaveUser;

public class PreferenceFragment extends PreferenceFragmentCompat {

    private static final String TAG = "PreferenceFragment" ;
    SharedPreferences.OnSharedPreferenceChangeListener listener;

    // firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private FirebaseUser authenticatedUser;
    private String authenticatedUserEmail;
    private ChildEventListener mChildEventListener;
    private Boolean authenticatedUserNewsletterPreference = false;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        // Firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("users");
        authenticatedUser = FirebaseAuth.getInstance().getCurrentUser();
        authenticatedUserEmail = authenticatedUser.getEmail();
        if (authenticatedUserEmail == null || authenticatedUserEmail.isEmpty()){
            for (UserInfo profile : authenticatedUser.getProviderData()){
                authenticatedUserEmail = profile.getEmail();
            }
        }

        // listen for users database changes to update authenticatedUserNewsletterPreference
        if (authenticatedUserEmail != null) {
            attachDatabaseReadListener();
        } else {
            detachDatabaseReadListener();
        }


        // Listen for changes to preferences and act on changes to synch with server
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals("enable_newsletter")) {

                    SaveUser saveUser = new SaveUser();
                    saveUser.setUserID(authenticatedUser.getUid());
                    saveUser.setUserName(authenticatedUser.getDisplayName());
                    saveUser.setUserEmail(authenticatedUserEmail);
                    saveUser.setUserEnableNewsLetter(sharedPreferences.getBoolean(key, false));
                    mUsersDatabaseReference.child(authenticatedUser.getUid()).setValue(saveUser)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Sync newsletter preferences successfull !");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, e.toString());
                                }
                            });
                }
            }
        };
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);

        // logout preference
        Preference logoutPreference = findPreference("logout_account");
        String authenticatedSummary = getString(R.string.account_already_login, FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
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

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);
        attachDatabaseReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener);
        detachDatabaseReadListener();
    }


    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    SaveUser saveUser = (SaveUser) dataSnapshot.getValue(SaveUser.class);
                    if (saveUser.getUserEmail().equals(authenticatedUserEmail)){
                        authenticatedUserNewsletterPreference = saveUser.getUserEnableNewsLetter();
                        CheckBoxPreference newsletterPreference = findPreference("enable_newsletter");
                        newsletterPreference.setChecked(authenticatedUserNewsletterPreference);

                    }
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    SaveUser saveUser = (SaveUser) dataSnapshot.getValue(SaveUser.class);
                    if (saveUser.getUserEmail().equals(authenticatedUserEmail)){
                        authenticatedUserNewsletterPreference = saveUser.getUserEnableNewsLetter();
                        CheckBoxPreference newsletterPreference = findPreference("enable_newsletter");
                        newsletterPreference.setChecked(authenticatedUserNewsletterPreference);

                    }
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };

            mUsersDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mUsersDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }



}
