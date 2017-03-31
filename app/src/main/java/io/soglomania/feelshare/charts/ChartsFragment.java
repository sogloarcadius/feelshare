package io.soglomania.feelshare.charts;

/**
 * Created by sogloarcadius on 12/03/17.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.soglomania.feelshare.R;
import io.soglomania.feelshare.main.MyApplication;
import io.soglomania.feelshare.model.SaveMood;


/**
 * A dummy fragment representing a section of the app, but that simply displays dummy text.
 * This would be replaced with your application's content.
 */
public class ChartsFragment extends Fragment {

    Realm realm;
    PieChart mChartWorld;
    PieChart mChartUser;

    public MyApplication context;

    private Integer[] moodsUID;
    private String[] moodsNames;


    private SharedPreferences sharedPref;
    private String email;

    private Typeface mTfRegular;
    private Typeface mTfLight;

    ArrayList<PieEntry> entriesWorld;
    PieDataSet dataSetWorld;
    PieData piedataWorld;

    ArrayList<PieEntry> entriesUser;
    PieDataSet dataSetUser;
    PieData piedataUser;


    public ChartsFragment() {
        // Empty constructor required for fragment subclasses
    }

    public static Fragment newInstance(String title) {
        Fragment fragment = new ChartsFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = ((MyApplication) getActivity().getApplicationContext());

        moodsUID = context.getMoodsUID();
        moodsNames = context.getMoodsNames();
        View rootView = inflater.inflate(R.layout.fragment_charts_layout, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);

        mTfRegular = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");

        realm = Realm.getDefaultInstance();

        mChartWorld = (PieChart) view.findViewById(R.id.chart);
        mChartUser = (PieChart) view.findViewById(R.id.chartuser);

        //mchartWorld

        configureWorldPieChart();

        //mchartUser

        configureUserPieChart();

        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm element) {
                setWorldData();
                mChartWorld.notifyDataSetChanged(); // let the chart know it's piedataWorld changed
                mChartWorld.invalidate(); // refresh

                setUserData();
                mChartUser.notifyDataSetChanged(); // let the chart know it's piedataWorld changed
                mChartUser.invalidate(); // refresh
            }
        });
    }

    @Override
    public void onStop() {
        realm.removeAllChangeListeners();
        super.onStop();

    }


    // ============================================ Custom methods  ==================================


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

        dataSetWorld = new PieDataSet(entriesWorld, "Moods");

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
        piedataWorld.setValueTextSize(11f);
        piedataWorld.setValueTextColor(Color.WHITE);
        piedataWorld.setValueTypeface(mTfLight);
        mChartWorld.setData(piedataWorld);

        // undo all highlights
        mChartWorld.highlightValues(null);

        mChartWorld.invalidate();
    }


    private void setUserData() {

        HashMap<Integer, Integer> counter = getUserCounter();
        entriesUser = new ArrayList<PieEntry>();

        // NOTE: The order of the entriesWorld when being added to the entriesWorld array determines their position around the center of
        // the chart.
        for (int i = 0; i < moodsUID.length; i++) {
            entriesUser.add(new PieEntry((float) (counter.get(moodsUID[i])),
                    moodsNames[moodsUID[i]]
            ));
        }

        dataSetUser = new PieDataSet(entriesUser, "Moods");

        dataSetUser.setDrawIcons(false);

        dataSetUser.setSliceSpace(3f);
        dataSetUser.setIconsOffset(new MPPointF(0, 40));
        dataSetUser.setSelectionShift(5f);

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

        dataSetUser.setColors(colors);
        //dataSetWorld.setSelectionShift(0f);

        piedataUser = new PieData(dataSetUser);
        piedataUser.setValueFormatter(new PercentFormatter());
        piedataUser.setValueTextSize(11f);
        piedataUser.setValueTextColor(Color.WHITE);
        piedataUser.setValueTypeface(mTfLight);
        mChartUser.setData(piedataUser);

        // undo all highlights
        mChartUser.highlightValues(null);

        mChartUser.invalidate();
    }


    private void configureWorldPieChart() {

        SpannableString world_chart_title = new SpannableString(getActivity().getResources().getString(R.string.chart2_title) + " \n" + getActivity().getResources().getString(R.string.chart2_desc));


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
        // enable rotation of the chart by touch
        mChartWorld.setRotationEnabled(true);
        mChartWorld.setHighlightPerTapEnabled(true);

        // mChartWorld.setUnit(" €");
        // mChartWorld.setDrawUnitsInChart(true);

        // add a selection listener

        setWorldData();

        mChartWorld.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChartWorld.spin(2000, 0, 360);


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

        //mChartWorld.setDrawSliceText(false);
        //piedataWorld.setDrawValues(false);


    }


    private void configureUserPieChart() {

        SpannableString user_chart_title = new SpannableString(getActivity().getResources().getString(R.string.chart1_title) + " \n" + getActivity().getResources().getString(R.string.chart1_desc));


        mChartUser.setUsePercentValues(true);
        mChartUser.getDescription().setEnabled(false);
        mChartUser.setExtraOffsets(5, 10, 5, 5);

        mChartUser.setDragDecelerationFrictionCoef(0.95f);

        mChartUser.setCenterTextTypeface(mTfLight);
        mChartUser.setCenterText(user_chart_title);

        mChartUser.setDrawHoleEnabled(true);
        mChartUser.setHoleColor(Color.WHITE);

        mChartUser.setTransparentCircleColor(Color.WHITE);
        mChartUser.setTransparentCircleAlpha(110);

        mChartUser.setHoleRadius(58f);
        mChartUser.setTransparentCircleRadius(61f);

        mChartUser.setDrawCenterText(true);

        mChartUser.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChartUser.setRotationEnabled(true);
        mChartUser.setHighlightPerTapEnabled(true);

        // mChartUser.setUnit(" €");
        // mChartUser.setDrawUnitsInChart(true);

        // add a selection listener

        setUserData();

        mChartUser.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChartWorld.spin(2000, 0, 360);


        Legend l = mChartUser.getLegend();

        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChartUser.setEntryLabelColor(Color.WHITE);
        mChartUser.setEntryLabelTypeface(mTfRegular);
        mChartUser.setEntryLabelTextSize(12f);

        //mChartUser.setDrawSliceText(false);
        //piedataUser.setDrawValues(false);


    }


    private HashMap<Integer, Integer> getWorldCounter() {
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        email = sharedPref.getString("email", null);
        HashMap<Integer, Integer> counter2 = new HashMap<Integer, Integer>();//world

        //init values to 0
        for (int i = 0; i < moodsUID.length; i++) {

            counter2.put(moodsUID[i], 0);
        }

        if (email != null) {

            // Build the query looking at all users:
            RealmQuery<SaveMood> queryall = realm.where(SaveMood.class);

            //  WORLD
            // Execute the query:
            RealmResults<SaveMood> result2 = queryall.findAll().sort("pk");
            for (SaveMood saveMood : result2) {

                int inc_value = counter2.get(saveMood.getMoodUID()) + 1;

                counter2.put(saveMood.getMoodUID(), inc_value);

            }
            Log.v("moodcounterWorld", counter2.toString());

        }

        return counter2;

    }


    private HashMap<Integer, Integer> getUserCounter() {
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        email = sharedPref.getString("email", null);
        HashMap<Integer, Integer> counter = new HashMap<Integer, Integer>();//user

        //init values to 0
        for (int i = 0; i < moodsUID.length; i++) {

            counter.put(moodsUID[i], 0);
        }

        if (email != null) {
            // Build the query looking at logged users:
            RealmQuery<SaveMood> queryuser = realm.where(SaveMood.class);

            // USER
            queryuser.equalTo("userID", email);
            // Add query conditions:
//            query.in("moodtype", new String[]{"Cool", "Happy"});

            // Execute the query:
            RealmResults<SaveMood> result1 = queryuser.findAll().sort("pk");
            for (SaveMood saveMood : result1) {

                int inc_value = counter.get(saveMood.getMoodUID()) + 1;

                counter.put(saveMood.getMoodUID(), inc_value);

            }
            Log.v("moodcounterUser", counter.toString());

        }

        return counter;

    }


}