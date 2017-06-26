package com.iuh.thach.newyorktimes.clients;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class ArticleClient {
    private static final String API_URL = "http://api.nytimes.com/svc/search/v2/articlesearch.json?api-key=";
    private static final String API_KEY = "b160974b41824c6ca23668924ef13e65";
    private AsyncHttpClient client;
    public ArticleClient(){
        client = new AsyncHttpClient();
    }

    private String getApiKey(String relativeKey){
        return API_URL+relativeKey;
    }
    private String getQuery(){
        return getApiKey(API_KEY);
    }

    public void getArticles(JsonHttpResponseHandler handler, RequestParams params){
        Log.i("Test",params.toString()+" "+getQuery());
        client.get(getQuery(), params, handler);
    }
}

