package presentation;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Created by sogloarcadius on 07/03/17.
 */

public class ArticleFragment extends Fragment {


    public static ArticleFragment create(String title) {
        Bundle args = new Bundle();
        args.putString("title", title);
        ArticleFragment fragment = new ArticleFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @SuppressLint("setJavaScriptEnabled")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final WebView webView = new WebView(getActivity());
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                webView.loadUrl(url);
//                return false;
//
//            }
//        });
        webView.loadUrl("file:///android_asset/" + getArguments().getString("title") + ".html");
        return webView;

    }

}
