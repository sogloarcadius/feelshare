package io.sogloarcadius.feelshare.feelings;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import io.sogloarcadius.feelshare.R;
import io.sogloarcadius.feelshare.main.MyApplication;
import io.sogloarcadius.feelshare.model.Moods;
import io.sogloarcadius.feelshare.model.SaveMood;


public class FeelingsFragment extends Fragment {


    CustomGridAdapter customGridAdapter;
    GridView gridview;

    private SharedPreferences sharedPref;
    private String email;

    private Boolean updateRealmDBIsSuccessful = false;
    int _position;


    MyApplication context;

    private String[] moodsNames;
    private Integer[] moodsImages;
    private Integer[] moodsUID;

    private String[] moodsDesc;


    public FeelingsFragment() {
        // Empty constructor required for fragment subclasses
    }

    public static Fragment newInstance(String title) {
        Fragment fragment = new FeelingsFragment();
        /*Bundle args = new Bundle();
        args.putString("str", title);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        context = ((MyApplication) getActivity().getApplicationContext());

        View rootView = inflater.inflate(R.layout.fragment_feelings_layout, container, false);
//        String title = getArguments().getString(TITLE);
//        getActivity().setTitle(title);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        moodsDesc = context.getMoodsDesc();
        moodsImages = context.getMoodsImages();
        moodsNames = context.getMoodsNames();
        moodsUID = context.getMoodsUID();

        final ArrayList<Moods> _moods = new ArrayList<Moods>();

        for (int i = 0; i < moodsDesc.length; i++) {
            Moods moods = new Moods();
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
                builder.setPositiveButton(R.string.dialog_log, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked send button
                        //UpdateRealmDB(moodsUID[_position]);

                    }
                });

                builder.setNegativeButton(R.string.dialog_share, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User click send and tweet the dialog
                        Boolean status = true;
                        //Boolean status = UpdateRealmDB(moodsUID[_position]);
                        if (status) {

                            List<Intent> targetShareIntents = new ArrayList<Intent>();
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            PackageManager pm = getActivity().getPackageManager();
                            List<ResolveInfo> resInfos = pm.queryIntentActivities(shareIntent, 0);
                            if (!resInfos.isEmpty()) {
                                System.out.println("Have package");
                                for (ResolveInfo resInfo : resInfos) {
                                    String packageName = resInfo.activityInfo.packageName;
                                    if (packageName.contains("com.twitter.android")) {
                                        Log.i("Package Name", packageName);
                                        Intent intent = new Intent();
                                        intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));

                                        String text = "FeelShare " + new Date().getYear();
                                        Uri imageUri = Uri.parse("android.resource://io.soglomania.feelshare/drawable/" + _moods.get(_position).getImg());
                                        intent.putExtra("AppName", resInfo.loadLabel(pm).toString());
                                        intent.putExtra(Intent.EXTRA_SUBJECT, _moods.get(_position).getName());
                                        intent.setAction(Intent.ACTION_SEND);
                                        intent.putExtra(Intent.EXTRA_TEXT, "\n\n" + "---\n" + text);
                                        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                                        intent.setType("image/*");
                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        intent.setPackage(packageName);
                                        targetShareIntents.add(intent);
                                    }
                                }
                                if (!targetShareIntents.isEmpty()) {
                                    Collections.sort(targetShareIntents, new Comparator<Intent>() {
                                        @Override
                                        public int compare(Intent o1, Intent o2) {
                                            return o1.getStringExtra("AppName").compareTo(o2.getStringExtra("AppName"));
                                        }
                                    });
                                    Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0), "Tweet it ?");
                                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
                                    startActivity(chooserIntent);
                                } else {
                                    Toast.makeText(getActivity(), getString(R.string.no_app), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                });

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

    @Override
    public void onStop() {
        /*
        if (realmAsyncTask != null && !realmAsyncTask.isCancelled()) {
            realmAsyncTask.cancel();
        }
        */
        super.onStop();
    }


}

