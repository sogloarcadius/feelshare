package io.sogloarcadius.feelshare.mood;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.sogloarcadius.feelshare.R;
import io.sogloarcadius.feelshare.main.FeelShareApplication;
import io.sogloarcadius.feelshare.model.Mood;
import io.sogloarcadius.feelshare.model.SaveMood;


public class MoodFragment extends Fragment {

    private static final String TAG = "FeelingsFragment";

    CustomGridAdapter customGridAdapter;
    GridView gridview;

    int _position;


    private FeelShareApplication context;

    private String[] moodsNames;
    private Integer[] moodsImages;
    private Integer[] moodsUID;

    private String[] moodsDesc;


    // firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMoodsDatabaseReference;


    public MoodFragment() {
        // Empty constructor required for fragment subclasses
    }

    public static Fragment newInstance(String title) {
        Fragment fragment = new MoodFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_feelings_layout, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMoodsDatabaseReference = mFirebaseDatabase.getReference().child("moods");

        context = ((FeelShareApplication) getActivity().getApplicationContext());

        moodsDesc = context.getMoodsDesc();
        moodsImages = context.getMoodsImages();
        moodsNames = context.getMoodsNames();
        moodsUID = context.getMoodsUID();

        final ArrayList<Mood> _moods = new ArrayList<Mood>();

        for (int i = 0; i < moodsDesc.length; i++) {
            Mood moods = new Mood();
            moods.setImg(moodsImages[i]);
            moods.setDesc(moodsDesc[i]);
            moods.setName(moodsNames[i]);
            moods.setUid(moodsUID[i]);
            _moods.add(moods);
        }


        gridview = (GridView) view.findViewById(R.id.gridview);

        customGridAdapter = new CustomGridAdapter(getActivity(), _moods);
        gridview.setAdapter(customGridAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                _position = position;
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss a");
                String strDate = sdf.format(c.getTime());


                //Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                //Chain together various setter methods to set the dialog characteristics
                builder.setIcon(moodsImages[_position])
                        .setTitle(moodsNames[_position])
                        .setMessage(moodsDesc[_position]);

                // Add the buttons
                builder.setPositiveButton(R.string.mood_send, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked send button
                        updateFirebaseDataBase(moodsUID[_position]);
                    }
                });

                builder.setNegativeButton(R.string.mood_cancel, null);

                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        SearchView inputSearch = (SearchView) view.findViewById(R.id.searchView1);
        inputSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                customGridAdapter.getFilter().filter(query);
                return false;
            }
        });


    }




    private void updateFirebaseDataBase(Integer id) {

        // device country
        String country = context.getDeviceCountry();

        // Email
        FirebaseUser authenticatedUser = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = authenticatedUser.getEmail();
        if (userEmail == null || userEmail.isEmpty()){
            for (UserInfo profile : authenticatedUser.getProviderData()){
                userEmail = profile.getEmail();
            }
        }

        // internet connection
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();

        if (connected) {
            long currentpk = (long) new Date().getTime();
            Log.d(TAG, String.valueOf(currentpk));

            final SaveMood saveMood = new SaveMood();
            saveMood.setUserID(userEmail);
            saveMood.setPk(currentpk);
            saveMood.setMoodUID(id);
            saveMood.setCountry(country);

            mMoodsDatabaseReference.child(String.valueOf(currentpk)).setValue(saveMood)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Write was successful!
                            Toast.makeText(getContext(), getString(R.string.dialog_log_message), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), getString(R.string.dialog_fail_log_message), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getContext(), getString(R.string.account_network_error), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
    }


}

