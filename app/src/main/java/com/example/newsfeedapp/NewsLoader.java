package com.example.newsfeedapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Ferhat on 8.5.2018.
 */

public class NewsLoader extends AsyncTaskLoader<List<NewsLibrary>> {

    private String getUrl;

    public NewsLoader(Context context, String url) {
        super(context);

        getUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsLibrary> loadInBackground() {
        if (getUrl == null) {
            return null;
        }

        List<NewsLibrary> newsFeed = NewsQuery.newsData(getUrl);
        return newsFeed;
    }
}
