package com.example.andralung.newsapp;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class NewsLoader extends AsyncTaskLoader<ArrayList<News>> {
    private static final String TAG = NewsAdapter.class.getSimpleName();

    private String query;
    private String lastQuery;
    private ArrayList<News> data;

    public NewsLoader(Context context, String query) {
        super(context);
        this.query = query;
    }


    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading() returned: " + query + " " + lastQuery + " " + data);
        if (query == null) return;
        if (query.isEmpty()) return;

        if (!query.equals(lastQuery)) {
            forceLoad();
        } else if (data == null || data.isEmpty()) {
            forceLoad();
        } else {
            deliverResult(data);
        }
    }

    @Override
    public ArrayList<News> loadInBackground() {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        String jsonResponse = "";

        try {
            String u = build(query);
            Log.d(TAG, "loadInBackground: " + u);
            URL url = new URL(u);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            Log.d(TAG, "loadInBackground: " + url + " " + urlConnection);

            if (urlConnection.getResponseCode() == 200) {

                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
                Log.d(TAG, "loadInBackground: ." + jsonResponse);
            } else {
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {

            Log.e(TAG, "Problem retrieving the news results.", e);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }

        if (TextUtils.isEmpty(jsonResponse)) return null;

        ArrayList<News> newsList = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse).getJSONObject("response");
            JSONArray results = baseJsonResponse.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject chosenSection = results.getJSONObject(i);
                String webTitle = chosenSection.getString("webTitle");
                String sectionName = chosenSection.getString("sectionName");
                String url = chosenSection.getString("webUrl");

                News news = new News(webTitle, sectionName, url);
                newsList.add(news);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the results", e);
        }

        lastQuery = query;
        return newsList;
    }

    public static String readFromStream(InputStream inputStream) {
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @Override
    public void deliverResult(ArrayList<News> data) {
        this.data = data;
        super.deliverResult(data);
    }

    private String build(String query) {
        return new Uri.Builder()
                .scheme("https")
                .authority("content.guardianapis.com")
                .path("search")
                .appendQueryParameter("q", query)
                .appendQueryParameter("tag", "sport/sport")
                .appendQueryParameter("api-key", "test")
                .build().toString();
    }
}

