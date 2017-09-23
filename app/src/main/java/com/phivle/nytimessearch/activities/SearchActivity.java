package com.phivle.nytimessearch.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.phivle.nytimessearch.R;
import com.phivle.nytimessearch.adapters.ArticleAdapter;
import com.phivle.nytimessearch.fragments.FilterFragment;
import com.phivle.nytimessearch.listeners.EndlessRecyclerViewScrollListener;
import com.phivle.nytimessearch.models.Article;
import com.phivle.nytimessearch.utils.Filters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cz.msebera.android.httpclient.Header;


public class SearchActivity extends AppCompatActivity {

    private final static String URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    private final static String API_KEY = "60cbef7d791f4f83be5a103be6ec458d";
    private final static String DEFAULT_PAGE = "0";
    private static final int TOO_MANY_REQUESTS = 429;

    private EditText mEtQuery;
    private RecyclerView mRvResults;
    private SharedPreferences mFilters;
    private EndlessRecyclerViewScrollListener mScrollListener;

    private List<Article> articles;
    private ArticleAdapter adapter;
    private ConnectivityManager mConnectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        mFilters = getSharedPreferences("filters", Context.MODE_PRIVATE);

        setupViews();

        articles = new ArrayList<>();
        adapter = new ArticleAdapter(this, articles);
        mRvResults.setAdapter(adapter);
        mRvResults.setLayoutManager(staggeredGridLayoutManager);
        mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        mScrollListener = new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {

            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
                Handler handler = new Handler();
                Runnable runnableCode = new Runnable() {
                    @Override
                    public void run() {
                        loadMore(page);
                    }
                };
                handler.postDelayed(runnableCode, 300);
            }

            private void loadMore(int page) {
                AsyncHttpClient client;

                RequestParams params = prepareParams(String.valueOf(page));

                client = new AsyncHttpClient();

                NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                if (isConnected) {

                    // notify user you are online

                    client.get(URL, params, new JsonHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            if (statusCode == TOO_MANY_REQUESTS) {
                                Toast.makeText(getApplicationContext(), "API Limit Reached", Toast.LENGTH_LONG).show();
                                mScrollListener.resetState();
                            }
                        }


                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            JSONArray articleJsonResults;

                            try {
                                articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                                List<Article> articleResults = Article.fromJsonArray(articleJsonResults);
                                int curSize = adapter.getItemCount();
                                articles.addAll(articleResults);
                                adapter.notifyItemRangeInserted(curSize, articleResults.size());
                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "No network connectivity", Toast.LENGTH_LONG).show();
                }
            }
        };

        mRvResults.addOnScrollListener(mScrollListener);

    }

    private RequestParams prepareParams(String page) {
        String beginDate = mFilters.getString("begin_date", null);
        String sortOrder = mFilters.getString("sort_order", null).toLowerCase();
        Set<String> newsDesk = mFilters.getStringSet("news_desk", null);
        String formattedNewsDesk = Filters.formatNewsDesk(newsDesk);

        RequestParams params = new RequestParams();
        String query = mEtQuery.getText().toString();

        params.put("api_key", API_KEY);
        params.put("page", page);
        params.put("q", query);
        params.put("begin_date", beginDate);
        params.put("sort", sortOrder);
        params.put("fq", formattedNewsDesk);

        return params;
    }

    private void setupViews() {
        mEtQuery = (EditText) findViewById(R.id.etQuery);
        mRvResults = (RecyclerView) findViewById(R.id.rvResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filter) {
            showFilterDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFilterDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterFragment filterFragment = FilterFragment.newInstance();
        filterFragment.show(fm, "filter");
    }

    private void search(RequestParams params) {
        AsyncHttpClient client;
        client = new AsyncHttpClient();

        NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            client.get(URL, params, new JsonHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    if (statusCode == TOO_MANY_REQUESTS) {
                        Toast.makeText(getApplicationContext(), "API Limit Reached", Toast.LENGTH_LONG).show();
                        mScrollListener.resetState();
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONArray articleJsonResults;

                    try {
                        articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                        List<Article> articleResults = Article.fromJsonArray(articleJsonResults);
                        articles.clear();
                        articles.addAll(articleResults);
                        adapter.notifyDataSetChanged();
                        mScrollListener.resetState();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(this, "No network connectivity", Toast.LENGTH_LONG).show();
        }
    }

}
