package com.iuh.thach.newyorktimes.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Article implements Parcelable {
    private String webURL;
    private String headLine;
    private String thumbNail;
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(webURL);
        dest.writeString(headLine);
        dest.writeString(thumbNail);
    }
    protected Article(Parcel in) {
        webURL = in.readString();
        headLine = in.readString();
        thumbNail = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };



    public String getWebURL() {
        return webURL;
    }

    public String getHeadLine() {
        return headLine;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public Article(){

    }
    private static Article fromJson(JSONObject jsonObject){

        Article info = new Article();
        try {
            info.webURL = jsonObject.getString("web_url");
            info.headLine = jsonObject.getJSONObject("headline").getString("main");
            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
            if(multimedia.length()>0){
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                info.thumbNail = "http://www.nytimes.com/"+multimediaJson.getString("url");
            }
            else{
                info.thumbNail = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return info;
    }
    public static ArrayList<Article> fromJSONArray(JSONArray jsonArray){
        ArrayList<Article> results = new ArrayList<>();
        for (int i = 0;i < jsonArray.length();i++){
            JSONObject articleJson;
            try {
                articleJson = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
            Article article = Article.fromJson(articleJson);
            results.add(article);
        }
        return results;
    }




}
