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

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    public static final String URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=10";
    private ListView earthquakeListView = null;
    private QuakeArrayAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
         earthquakeListView = (ListView) findViewById(R.id.list);

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

        EarthQuakeAsyncTask task = new EarthQuakeAsyncTask();
        task.execute(URL);


    }

    private class EarthQuakeAsyncTask extends AsyncTask<String, Void, ArrayList<Quake>>{


        @Override
        protected ArrayList<Quake> doInBackground(String... url) {
            if (url.length < 1 || url[0] == null) {
                return  null;
            }
            // Create a fake list of earthquake locations.

            return QueryUtils.fetchEarthquakeData(url[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Quake> quakes) {
            adapter.clear();

            if (quakes!=null && !quakes.isEmpty()){
                adapter.addAll(quakes);
            }

        }
    }
}
