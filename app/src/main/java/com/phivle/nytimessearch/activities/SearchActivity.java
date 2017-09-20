package com.phivle.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.phivle.nytimessearch.R;
import com.phivle.nytimessearch.adapters.ArticleAdapter;
import com.phivle.nytimessearch.models.Article;
import com.phivle.nytimessearch.models.Filters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    private final static String URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    private final static String API_KEY = "60cbef7d791f4f83be5a103be6ec458d";
    private final static String DEFAULT_PAGE = "0";

    private EditText mEtQuery;
    private RecyclerView mRvResults;
    private Button mBtnSearch;


    private List<Article> articles;
    private ArticleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupViews();

        articles = new ArrayList<>();
        adapter = new ArticleAdapter(this, articles);
        mRvResults.setAdapter(adapter);
        mRvResults.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

    }

    public void setupViews() {
        mEtQuery = (EditText) findViewById(R.id.etQuery);
        mRvResults = (RecyclerView) findViewById(R.id.rvResults);
        mBtnSearch = (Button) findViewById(R.id.btnSearch);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_filter) {
            launchFilterAction();
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchFilterAction() {
        Intent filterSearchIntent = new Intent(this, FilterActivity.class);
        startActivity(filterSearchIntent);
    }

    public void onArticleSearch(View view) {
        String query;
        AsyncHttpClient client;

        RequestParams params = new RequestParams();
        query = mEtQuery.getText().toString();
        params.put("api_key", API_KEY);
        params.put("page", DEFAULT_PAGE);
        params.put("q", query);
        params.put("begin_date", Filters.getBeginDateFilter());
        params.put("sort", Filters.getSortOrder());
        params.put("fq", Filters.getNewsDesk());

        client = new AsyncHttpClient();

        client.get(URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults;

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    List<Article> articleResults = Article.fromJsonArray(articleJsonResults);
                    articles.addAll(articleResults);
                    adapter.notifyItemRangeInserted(adapter.getItemCount(), articleResults.size());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
