package com.phivle.nytimessearch.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.phivle.nytimessearch.R;
import com.phivle.nytimessearch.models.Article;

import org.parceler.Parcels;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Article article;
        WebView webView;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        article = Parcels.unwrap(getIntent().getParcelableExtra("article"));

        webView = (WebView) findViewById(R.id.wvArticle);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl(article.getWebUrl());

    }
}
