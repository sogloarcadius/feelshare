package io.sogloarcadius.feelshare.support;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
