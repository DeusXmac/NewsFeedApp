package com.example.newsfeedapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsLibrary>>{

    @BindView(R.id.listNews) ListView newsFeed;
    @BindView(R.id.empty) TextView empty;
    @BindView(R.id.indicator) View indicator;

    private NewsAdapter items;

    private static final String GUARDIAN_URL =
            "https://content.guardianapis.com/search?&show-tags=contributor&api-key=5c334dc1-26b5-4609-b56a-8d854d21a6ea&show-fields=thumbnail";

    private static final int NEWS_LOADER = 1;

    private static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ButterKnife.bind(this);

        items = new NewsAdapter(this, new ArrayList<NewsLibrary>());

        newsFeed.setAdapter(items);

        newsFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NewsLibrary getItem = items.getItem(position);

                Uri uri = Uri.parse(getItem.getWebUrl());

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                if (intent.resolveActivity(getPackageManager()) != null) {

                startActivity(intent);
                }
            }
        });

        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo network = manager.getActiveNetworkInfo();

        if (network != null && network.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER, null, this);

        } else {
            indicator.setVisibility(View.GONE);
            empty.setText(R.string.noInternet);}
    }

    @Override
    public Loader<List<NewsLibrary>> onCreateLoader(int id, Bundle args) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String search = sharedPreferences.getString("search",null);

        String orderBy = sharedPreferences.getString("orderBy",null);

        Uri baseUri = Uri.parse(GUARDIAN_URL);

        Uri.Builder uriBuilder = baseUri.buildUpon();

        if(search != null) {
            uriBuilder.appendQueryParameter("q",search);
        }

        if(orderBy != null) {
            uriBuilder.appendQueryParameter("order-by", orderBy);
        }

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsLibrary>> loader, List<NewsLibrary> data) {

        indicator.setVisibility(View.GONE);

        items.clear();

        if(data != null && !data.isEmpty()) {
            items.addAll(data);
        } else {
            empty.setText(R.string.noNews);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsLibrary>> loader) {
        items.clear();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settings = new Intent(NewsActivity.this, NewsSettings.class);
            startActivity(settings);
            finish();
            return true;
        }
        if(id == R.id.exit) {
            if(sharedPreferences != null) {
                sharedPreferences.edit().clear().commit(); }
            System.exit(0);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
