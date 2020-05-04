package io.sogloarcadius.feelshare.support;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


public class ArticleFragment extends Fragment {


    public static ArticleFragment create(String file) {
        Bundle args = new Bundle();
        args.putString("file", file);

        ArticleFragment fragment = new ArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @SuppressLint("setJavaScriptEnabled")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final WebView webView = new WebView(getActivity());
        webView.loadUrl("file:///android_asset/" + getArguments().getString("file") + ".html");
        return webView;

    }

}
