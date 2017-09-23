package com.phivle.nytimessearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Random;

public class Article implements Parcelable {

    private static final String NYTIMES_URL = "https://www.nytimes.com/";
    private String mWebUrl;
    private String mHeadline;
    private String mThumbnail;

    private Article(Parcel in) {
        mWebUrl = in.readString();
        mHeadline = in.readString();
        mThumbnail = in.readString();
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

    public String getWebUrl() {
        return mWebUrl;
    }

    public String getHeadline() {
        return mHeadline;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    private Article(JSONObject jsonObject) {
        try {
            this.mWebUrl = jsonObject.getString("web_url");
            this.mHeadline = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            if (multimedia.length() > 0) {
                JSONObject multimediaJson = multimedia.getJSONObject(new Random().nextInt(multimedia.length()));
                this.mThumbnail = String.format("%s%s", NYTIMES_URL, multimediaJson.getString("url"));
            } else {
                this.mThumbnail = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Article> fromJsonArray(JSONArray jsonArray) {
        ArrayList<Article> results = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            try {
                results.add(new Article(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mWebUrl);
        parcel.writeString(mHeadline);
        parcel.writeString(mThumbnail);
    }
}
