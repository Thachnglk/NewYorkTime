package com.iuh.thach.newyorktimes.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.iuh.thach.newyorktimes.R;
import com.iuh.thach.newyorktimes.adapters.ArticleArrayAdapter;
import com.iuh.thach.newyorktimes.adapters.EndlessScrollListener;
import com.iuh.thach.newyorktimes.clients.ArticleClient;
import com.iuh.thach.newyorktimes.fragment.FilterSearchFragment;
import com.iuh.thach.newyorktimes.models.Article;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;



//question: ask about infinite scroll ?
public class ListActivity extends AppCompatActivity implements FilterSearchFragment.onFilterSelected{
    private GridView gvResults;
    private ArrayList<Article> articlesArray;
    private ArticleArrayAdapter articleAdapter;
    private ArticleClient client;
    private SwipeRefreshLayout swipeContainer;
    private String textQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        isOnline();
        setupsView();
        fetchArticles("", 0);
        onSelectedFilter();
        onScrollViewMore();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_title);
    }

    private int currentPage = 0;

    private void onScrollViewMore() {
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                fetchArticles("", page);

                return true;
            }
        });
    }

    private void fetchArticles(String query, int page) {
        if (page == 0) articleAdapter.clear();
        RequestParams params = new RequestParams();
        params.put("page", page);
        if(!TextUtils.isEmpty(query)){
            params.put("q",query);
        }
        client = new ArticleClient();
        client.getArticles(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                fetchData(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ListActivity.this, "Fail...", Toast.LENGTH_SHORT).show();
            }
        }, params);

    }
    private void fetchData(JSONObject response){
        Log.d("123", response.toString());
        try {
            JSONObject jsonObject = response.getJSONObject("response");
            JSONArray docs;
            docs = jsonObject.getJSONArray("docs");
            final ArrayList<Article> articles = Article.fromJSONArray(docs);
            for (Article article : articles) {
                articleAdapter.addAll(article);
            }
            articleAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupsView() {
        gvResults = (GridView) findViewById(R.id.gvResults);
        articlesArray = new ArrayList<>();
        articleAdapter = new ArticleArrayAdapter(this,articlesArray);
        gvResults.setAdapter(articleAdapter);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout();
            }
        });
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article article = articlesArray.get(position);
                Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                i.putExtra("url", article.getWebURL());
                startActivity(i);
            }
        });
    }
    private void swipeLayout() {
        fetchArticles("", 0);
        swipeContainer.setRefreshing(false);
    }

    private void onSelectedFilter() {
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.searchIcon);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                articleAdapter.clear();
                getFilterSearch(query, dateForQuery, sortForQuery);
                textQuery = query;
                swipeContainer.setRefreshing(false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.setting){
            onClickItemSetting();
        }
        return super.onOptionsItemSelected(item);
    }

    private void onClickItemSetting() {
        FilterSearchFragment df = new FilterSearchFragment();
        FragmentManager fm = getSupportFragmentManager();
        df.show(fm,"filter_search");
    }

    private void getFilterSearch(String query,String date,String sort) {
        articleAdapter.clear();
        client = new ArticleClient();
        RequestParams params = new RequestParams();
        if(!TextUtils.isEmpty(query)){
            params.put("q",query);
        }
        params.put("begin_date",date);
        params.put("sort",sort);
        client.getArticles(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                fetchData(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ListActivity.this, "Failed...", Toast.LENGTH_SHORT).show();
            }
        }, params);

    }
    private String dateForQuery;
    private String sortForQuery;
    @Override
    public void setQueryFilterInterface(String date, String sort) {
        dateForQuery = date;
        sortForQuery = sort;
        Log.i("LoggerDate",date);
        getFilterSearch("",date, sort);
    }
    public boolean isOnline(){
        Runtime runtime = Runtime.getRuntime();
        try{
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return exitValue == 0;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
    private StringBuilder stringBuilder;
    public void checkInternet(){
        try {
            URL url = new URL("http://www.google.com");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            InputStream in = conn.getInputStream();
            stringBuilder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private class NetWorkAsync extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }
}
