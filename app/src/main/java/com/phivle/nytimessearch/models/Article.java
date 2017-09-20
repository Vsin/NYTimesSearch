package com.phivle.nytimessearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Vsin on 9/17/17.
 */

public class Article implements Serializable {

    private static final String NYTIMES_URL = "https://www.nytimes.com";
    private String mWebUrl;
    private String mHeadline;
    private String mThumbnail;

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
                JSONObject multimediaJson = multimedia.getJSONObject(0);
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
}
