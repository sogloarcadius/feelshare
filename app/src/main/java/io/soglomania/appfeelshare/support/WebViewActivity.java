package io.soglomania.appfeelshare.support;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       setTitle(getIntent().getStringExtra("title").toUpperCase());

        ArticleFragment fragment = ArticleFragment.create(getIntent().getStringExtra("title"));

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, fragment)
                .commit();

    }

}
