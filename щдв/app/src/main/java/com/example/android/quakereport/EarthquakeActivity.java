/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Quake>> {

    public static final String LOG_TAG = "myLogs";
            //EarthquakeActivity.class.getName();
    public static final String URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=10";
    private ListView earthquakeListView = null;
    private TextView emptyView;
    private ProgressBar progressBar;
    private QuakeArrayAdapter adapter;
    /**
     * Constant value for Earhquake loader id
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
         earthquakeListView = (ListView) findViewById(R.id.list);

        //find and set TextView for emptyView
        emptyView = (TextView)findViewById(R.id.empty);
        earthquakeListView.setEmptyView(emptyView);

        progressBar = (ProgressBar)findViewById(R.id.progress_bar);

        adapter = new QuakeArrayAdapter(this, 0, new ArrayList<Quake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Quake currentQuake = adapter.getItem(i);
                String url = currentQuake.getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        Log.d(LOG_TAG,"Init Loader");
        NetworkInfo networkInfo = getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)){
            emptyView.setText(R.string.no_network);
            progressBar.setVisibility(View.GONE);
        } else getLoaderManager().initLoader(EARTHQUAKE_LOADER_ID,null,this);

    }

    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }


    @Override
    public Loader<List<Quake>> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG,"OnCreateLoader Callback");
        return new EarthquakeLoader(this, URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Quake>> loader, List<Quake> quakes) {
        Log.d(LOG_TAG,"OnLoadFinished Callback");
        adapter.clear();
        progressBar.setVisibility(View.GONE);
        emptyView.setText(R.string.empty);

        if (quakes!=null && !quakes.isEmpty()){
            adapter.addAll(quakes);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Quake>> loader) {
        Log.d(LOG_TAG,"onLoaderReset Callback");
        adapter.clear();

    }
}
