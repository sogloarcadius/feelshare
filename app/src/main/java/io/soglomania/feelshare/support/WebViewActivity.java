package io.soglomania.feelshare.support;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class WebViewActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setTitle(getIntent().getStringExtra("title").toUpperCase());

        ArticleFragment fragment = ArticleFragment.create(getIntent().getStringExtra("title"));

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();

    }

}
