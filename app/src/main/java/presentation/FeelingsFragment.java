package presentation;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import domain.Moods;
import io.soglomania.feelshare.R;


/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class FeelingsFragment extends Fragment {


    CustomGridAdapter customGridAdapter;
    GridView gridview;
    Dialog dialog;

    int _position;

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

        View rootView = inflater.inflate(R.layout.fragment_feelings_layout, container, false);
//        String title = getArguments().getString(TITLE);
//        getActivity().setTitle(title);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //references to our images
        final Integer[] moodsImages = {
                R.drawable.afraid,
                R.drawable.angry,
                R.drawable.bored,
                R.drawable.childish,
                R.drawable.confused,
                R.drawable.cool,
                R.drawable.crying,
                R.drawable.depressed,
                R.drawable.excited,
                R.drawable.frustrated,
                R.drawable.funny,
                R.drawable.happy,
                R.drawable.hungry,
                R.drawable.neutral,
                R.drawable.romantic,
                R.drawable.sad,
                R.drawable.scared,
                R.drawable.shy,
                R.drawable.sick,
                R.drawable.sleepy,
                R.drawable.surprised,
                R.drawable.tired,
        };

        // ==references to our images title

        String[] moodsNames = {
                "Afraid :" + getResources().getString(R.string.afraid_desc),
                "Angry :" + getResources().getString(R.string.angry_desc),
                "Bored :" + getResources().getString(R.string.bore_desc),
                "Naughty :" + getResources().getString(R.string.naughty_desc),
                "Confused :" + getResources().getString(R.string.confused_desc),
                "Cool :" + getResources().getString(R.string.cool_desc),
                "Crying :" + getResources().getString(R.string.crying_desc),
                "Depressed :" + getResources().getString(R.string.depression_desc),
                "Excited :" + getResources().getString(R.string.excited_desc),
                "Frustated :" + getResources().getString(R.string.frustated_desc),
                "Funny :" + getResources().getString(R.string.funny_desc),
                "Happy :" + getResources().getString(R.string.happy_desc),
                "Hungry :" + getResources().getString(R.string.hungry_desc),
                "Neutral :" + getResources().getString(R.string.neutral_desc),
                "Romantic :" + getResources().getString(R.string.romantic_desc),
                "Sad :" + getResources().getString(R.string.sad_desc),
                "Scared :" + getResources().getString(R.string.scared_desc),
                "Shy :" + getResources().getString(R.string.shy_desc),
                "Sick :" + getResources().getString(R.string.sick_desc),
                "Sleepy :" + getResources().getString(R.string.sleepy_desc),
                "Surprised :" + getResources().getString(R.string.surprised_desc),
                "Tired :" + getResources().getString(R.string.tired_desc)
        };

        final ArrayList<Moods> _moods = new ArrayList<Moods>();

        for (int i = 0; i < moodsImages.length; i++) {
            Moods moods = new Moods();
            moods.setId(moodsImages[i]);
            moods.setDesc(moodsNames[i]);
            _moods.add(moods);
        }


        gridview = (GridView) view.findViewById(R.id.gridview);

        customGridAdapter = new CustomGridAdapter(getActivity(), _moods);
        gridview.setAdapter(customGridAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                _position = position;
                //custom dialog
                dialog = new Dialog(getContext());

                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss a");
                String strDate = sdf.format(c.getTime());

                dialog.setContentView(R.layout.custom_dialog);
                dialog.setTitle(strDate);

                ImageView imageView = (ImageView) dialog.findViewById(R.id.dialogImageMood);
                imageView.setImageResource(moodsImages[position]);

                Button dialogButtonlog = (Button) dialog.findViewById(R.id.dialogButtonLog);
                // if button is clicked, close the custom dialog
                dialogButtonlog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), getString(R.string.dialog_log_message), Toast.LENGTH_LONG).show();
                    }
                });


                Button dialogButtonlog_share = (Button) dialog.findViewById(R.id.dialogButtonLog_share);
                // if button is clicked, close the custom dialog
                dialogButtonlog_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), getString(R.string.dialog_log_message), Toast.LENGTH_SHORT).show();


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

                                if (packageName.contains("com.twitter.android") || packageName.contains("com.facebook.katana")
                                        || packageName.contains("com.whatsapp") || packageName.contains("com.google.android.apps.plus")
                                        || packageName.contains("com.google.android.talk") || packageName.contains("com.slack")
                                        || packageName.contains("com.google.android.gm") || packageName.contains("com.facebook.orca")
                                        || packageName.contains("com.yahoo.mobile") || packageName.contains("com.skype.raider")
                                        || packageName.contains("com.android.mms") || packageName.contains("com.linkedin.android")
                                        || packageName.contains("com.google.android.apps.messaging")) {
                                    Log.i("Package Name", packageName);
                                    Intent intent = new Intent();
                                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));


                                    String text = "Shared from FeelShare ";
                                    Uri imageUri = Uri.parse("android.resource://fr.labsoglo.feelshare/drawable/" + _moods.get(_position).getId());
                                    intent.putExtra("AppName", resInfo.loadLabel(pm).toString());

                                    intent.setAction(Intent.ACTION_SEND);
                                    intent.putExtra(Intent.EXTRA_SUBJECT, text);
                                    intent.putExtra(Intent.EXTRA_TEXT, _moods.get(_position).getDesc() + "\n\n" + "---\n" + "https://apps.soglomania.io/");
                                    intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                                    intent.setType("image");
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
                                Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0), "Select app to share");
                                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
                                startActivity(chooserIntent);
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.no_app), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

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
}

