package io.sogloarcadius.feelshare.chart;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
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
import io.sogloarcadius.feelshare.R;
import io.sogloarcadius.feelshare.main.FeelShareApplication;
import io.sogloarcadius.feelshare.model.SaveMood;

public class WorldChartFragment extends Fragment {

    private static final String TAG = "WorldChartFragment" ;
    PieChart mChartWorld;

    private FeelShareApplication context;

    private Integer[] moodsUID;
    private String[] moodsNames;


    private Typeface mTfRegular;
    private Typeface mTfLight;

    ArrayList<PieEntry> entriesWorld;
    PieDataSet dataSetWorld;
    PieData piedataWorld;

    // firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMoodsDatabaseReference;
    private ChildEventListener mChildEventListener;
    private String authenticatedUserEmail;
    List<SaveMood> moods = new ArrayList<>();



    public WorldChartFragment() {
        // Empty constructor required for fragment subclasses
    }

    public static Fragment newInstance(String title) {
        Fragment fragment = new WorldChartFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = ((FeelShareApplication) getActivity().getApplicationContext());

        moodsUID = context.getMoodsUID();
        moodsNames = context.getMoodsNames();
        View rootView = inflater.inflate(R.layout.fragment_charts_world_layout, container, false);
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

        mTfRegular = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");

        mChartWorld = (PieChart) view.findViewById(R.id.chart);

        //mchartWorld
        configureWorldPieChart();


    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListener();
        moods.clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        detachDatabaseReadListener();
        moods.clear();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    SaveMood moodAdded = dataSnapshot.getValue(SaveMood.class);
                        moods.add(moodAdded);
                        setWorldData();
                        mChartWorld.notifyDataSetChanged();
                        mChartWorld.invalidate();
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

    private void configureWorldPieChart() {

        SpannableString world_chart_title = new SpannableString(getActivity().getResources().getString(R.string.chart2_title) + " \n" + getActivity().getResources().getString(R.string.chart2_desc));

        // all possible touch-interactions with the chart
        mChartWorld.setTouchEnabled(true);

        // rotation of the chart by touch
        mChartWorld.setRotationEnabled(false);

        mChartWorld.setUsePercentValues(true);
        mChartWorld.getDescription().setEnabled(false);
        mChartWorld.setExtraOffsets(5, 10, 5, 5);

        mChartWorld.setDragDecelerationFrictionCoef(0.95f);

        mChartWorld.setCenterTextTypeface(mTfLight);
        mChartWorld.setCenterText(world_chart_title);

        mChartWorld.setDrawHoleEnabled(true);
        mChartWorld.setHoleColor(Color.WHITE);

        mChartWorld.setTransparentCircleColor(Color.WHITE);
        mChartWorld.setTransparentCircleAlpha(110);

        mChartWorld.setHoleRadius(58f);
        mChartWorld.setTransparentCircleRadius(61f);

        mChartWorld.setDrawCenterText(true);

        mChartWorld.setRotationAngle(0);
        mChartWorld.setHighlightPerTapEnabled(true);

        // add data
        setWorldData();

        Legend l = mChartWorld.getLegend();

        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChartWorld.setEntryLabelColor(Color.WHITE);
        mChartWorld.setEntryLabelTypeface(mTfRegular);
        mChartWorld.setEntryLabelTextSize(12f);

        // labels
        mChartWorld.setDrawEntryLabels(false);
        piedataWorld.setDrawValues(false);

    }



    private void setWorldData() {

        HashMap<Integer, Integer> counter = getWorldCounter();
        entriesWorld = new ArrayList<PieEntry>();

        // NOTE: The order of the entriesWorld when being added to the entriesWorld array determines their position around the center of
        // the chart.
        for (int i = 0; i < moodsUID.length; i++) {
            entriesWorld.add(new PieEntry((float) (counter.get(moodsUID[i])),
                    moodsNames[moodsUID[i]]
            ));
        }

        dataSetWorld = new PieDataSet(entriesWorld, getString(R.string.moods));

        dataSetWorld.setDrawIcons(false);

        dataSetWorld.setSliceSpace(3f);
        dataSetWorld.setIconsOffset(new MPPointF(0, 40));
        dataSetWorld.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSetWorld.setColors(colors);
        //dataSetWorld.setSelectionShift(0f);

        piedataWorld = new PieData(dataSetWorld);
        piedataWorld.setValueFormatter(new PercentFormatter());

        // remove percentage in piechart
        piedataWorld.setValueTextSize(0);

        piedataWorld.setValueTextColor(Color.WHITE);
        piedataWorld.setValueTypeface(mTfLight);
        mChartWorld.setData(piedataWorld);

        // undo all highlights
        mChartWorld.highlightValues(null);

        mChartWorld.invalidate();
    }




    private HashMap<Integer, Integer> getWorldCounter() {

        HashMap<Integer, Integer> counterWorld = new HashMap<Integer, Integer>();//world
        //init values to 0
        for (int i = 0; i < moodsUID.length; i++) {

            counterWorld.put(moodsUID[i], 0);
        }

        if (authenticatedUserEmail != null) {
            for (SaveMood saveMood : moods) {
                int inc_value = counterWorld.get(saveMood.getMoodUID()) + 1;
                counterWorld.put(saveMood.getMoodUID(), inc_value);
            }
            Log.v(TAG, counterWorld.toString());

        }

        return counterWorld;

    }


}