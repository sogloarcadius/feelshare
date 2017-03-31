package io.soglomania.feelshare.support;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.soglomania.feelshare.R;


/**
 * Created by sogloarcadius on 12/03/17.
 */

public class SupportFragment extends Fragment {

    String[][] data;

    public SupportFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings_layout, container, false);
        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView vue;
        vue = (ListView) view.findViewById(R.id.listView);

        data = new String[][]{
                {getString(R.string.conditions_title), getString(R.string.conditions_desc)},
                {getString(R.string.help_title), getString(R.string.help_desc)},
                {getString(R.string.about_title), getString(R.string.about_desc)}};


        List<HashMap<String, String>> liste = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> element;

        for (int i = 0; i < data.length; i++) {
            element = new HashMap<String, String>();
            element.put("text1", data[i][0]);
            element.put("text2", data[i][1]);
            liste.add(element);
        }

        ListAdapter adapter = new SimpleAdapter(getActivity(), liste, android.R.layout.simple_list_item_2,
                new String[]{"text1", "text2"},
                new int[]{android.R.id.text1, android.R.id.text2});
        vue.setAdapter(adapter);

        vue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //conditions
                if (position == 0) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("title", data[0][0].toLowerCase());
                    startActivity(intent);
                }

                //help

                else if (position == 1) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("title", data[1][0].toLowerCase());
                    startActivity(intent);

                }

                //about
                else if (position == 2) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("title", data[2][0].toLowerCase());
                    startActivity(intent);

                }

            }
        });


    }
}
