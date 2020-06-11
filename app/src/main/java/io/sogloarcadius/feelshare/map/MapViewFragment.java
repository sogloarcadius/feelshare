package io.sogloarcadius.feelshare.map;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.sogloarcadius.feelshare.R;
import io.sogloarcadius.feelshare.main.FeelShareApplication;
import io.sogloarcadius.feelshare.model.TopMood;
import io.sogloarcadius.feelshare.model.SaveMood;

public class MapViewFragment extends Fragment {

    private static final String TAG = "MapViewFragment";


    private FeelShareApplication context;
    private HashMap<String, TopMood> markers;
    private Integer[] moodsUID;
    private String[] moodsNames;
    private Integer[] moodsImages;

    private MapView mMapView;
    private GoogleMap mMap;

    // firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMoodsDatabaseReference;
    private ChildEventListener mChildEventListener;
    private String authenticatedUserEmail;
    private List<SaveMood> moods = new ArrayList<>();


    public MapViewFragment() {
        // Empty constructor required for fragment subclasses
    }

    public static Fragment newInstance(String title) {
        Fragment fragment = new MapViewFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_view_layout, container, false);


        context = ((FeelShareApplication) getActivity().getApplicationContext());

        moodsUID = context.getMoodsUID();
        moodsNames = context.getMoodsNames();
        moodsImages = context.getMoodsImages();


        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        // needed to get the map to display immediately
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;


                mMap.setMinZoomPreference(1.0f);  // world zoom
                mMap.setMaxZoomPreference(5.0f);  // continent zoom

                // zoom and gestures controls
                mMap.getUiSettings().setZoomControlsEnabled(false);
                mMap.getUiSettings().setMapToolbarEnabled(false);
                mMap.getUiSettings().setRotateGesturesEnabled(false);
                mMap.getUiSettings().setZoomGesturesEnabled(true);

            }
        });

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMoodsDatabaseReference = mFirebaseDatabase.getReference().child("moods");
        FirebaseUser authenticatedUser = FirebaseAuth.getInstance().getCurrentUser();
        authenticatedUserEmail = authenticatedUser.getEmail();
        if (authenticatedUserEmail == null || authenticatedUserEmail.isEmpty()){
            for (UserInfo profile : authenticatedUser.getProviderData()){
                authenticatedUserEmail = profile.getEmail();
            }
        }
        if (authenticatedUserEmail != null) {
            attachDatabaseReadListener();
        } else {
            detachDatabaseReadListener();
            moods.clear();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        detachDatabaseReadListener();
        moods.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        attachDatabaseReadListener();

    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
        detachDatabaseReadListener();
        moods.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        detachDatabaseReadListener();
        moods.clear();
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    SaveMood moodAdded = dataSnapshot.getValue(SaveMood.class);
                    moods.add(moodAdded);
                    refreshMap();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };

            mMoodsDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mMoodsDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }


    private List<TopMood> topMoodByCountry() {

        HashMap<String, HashMap<Integer, Integer>> allCountryCounter = new HashMap<String, HashMap<Integer, Integer>>();

        //init values to 0
        for (String country: context.getAllCountryLatLng().keySet()){
            HashMap<Integer, Integer> countryCounter = new HashMap<Integer, Integer>();
            for (int i = 0; i < moodsUID.length; i++) {
                countryCounter.put(moodsUID[i], 0);
            }
            allCountryCounter.put(country, countryCounter);
        }

        // count moods per country
        if (authenticatedUserEmail != null) {
            for (SaveMood saveMood : moods) {
                HashMap<Integer, Integer> currentCountryMoodCounter = allCountryCounter.get(saveMood.getCountry());
                if (currentCountryMoodCounter != null){
                    int inc_value = currentCountryMoodCounter.get(saveMood.getMoodUID()) + 1;
                    currentCountryMoodCounter.put(saveMood.getMoodUID(), inc_value);
                    allCountryCounter.put(saveMood.getCountry(), currentCountryMoodCounter);
                }
            }
        }

        // get max mood per country
        // ignore if there is no mood yet for this country
        // because we initialize all moods counters by country to 0
        // more elegant solution available in java 8, but need min api level 14
        List<TopMood> mGoogleMapMarkersData = new ArrayList<TopMood>();

        for ( String country: allCountryCounter.keySet() ) {
            Map.Entry<Integer, Integer> maxEntry = null;
            for (Map.Entry<Integer, Integer> entry : allCountryCounter.get(country).entrySet()) {
                if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                    maxEntry = entry;
                }
            }
            if (maxEntry != null) {
                if (maxEntry.getValue() > 0) {
                    TopMood topMood = new TopMood();
                    topMood.setCountry(country);
                    topMood.setTopMoodID(maxEntry.getKey());
                    topMood.setTopMoodName(moodsNames[maxEntry.getKey()]);
                    topMood.setTopMoodImage(moodsImages[maxEntry.getKey()]);
                    mGoogleMapMarkersData.add(topMood);
                }
            }
        }


        return mGoogleMapMarkersData;
    }

    private void refreshMap(){
        if (mMapView !=  null && mMap != null) {
            markers = new HashMap<String, TopMood>();

            List<TopMood> topMoods = topMoodByCountry();

            for (TopMood topMood: topMoods) {
                Log.v(TAG, topMood.toString());
                MarkerOptions markerOptions = new MarkerOptions().position(context.getCountryLatLng(topMood.getCountry()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.round_beenhere_black_18));
                Marker marker = mMap.addMarker(markerOptions);
                marker.setTag(topMood.getCountry());
                markers.put((String) marker.getTag(), topMood);
            }


            //mMap.moveCamera(CameraUpdateFactory.newLatLng(context.getCountryLatLng(context.getDeviceCountry())));
            //  animate camera move
            CameraPosition cameraPosition = new CameraPosition.Builder().target(context.getCountryLatLng(context.getDeviceCountry())).zoom(1.0f).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    displayMapInfo(markers.get(marker.getTag()));
                    return false;
                }
            });
        }
    }

    private void displayMapInfo(TopMood mapInfo){

        if (mapInfo != null) {
            new AlertDialog.Builder(getActivity())
                    .setIcon(mapInfo.getTopMoodImage())
                    .setTitle(Html.fromHtml(getString(R.string.map_info, new Locale("", mapInfo.getCountry()).getDisplayCountry(), mapInfo.getTopMoodName())))
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity();
                        }
                    })
                    .show();
        }
    }
}
