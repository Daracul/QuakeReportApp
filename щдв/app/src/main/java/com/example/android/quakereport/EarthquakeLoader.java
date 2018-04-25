package com.example.android.quakereport;


import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;


import java.util.List;

/**
 * Created by amalakhov on 23.04.2018.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Quake>> {
    private String url;

    public EarthquakeLoader(Context context, String url) {
        super(context);
        this.url=url;
    }

    @Override
    protected void onStartLoading() {
        Log.d(EarthquakeActivity.LOG_TAG,"Loader StartLoading method");
        forceLoad();
    }

    @Override
    public List<Quake> loadInBackground() {
        Log.d(EarthquakeActivity.LOG_TAG,"Loader loadInBackground method");
        if (url == null) {
            return  null;
        }
        // Create a fake list of earthquake locations.

        return QueryUtils.fetchEarthquakeData(url);
    }
}
