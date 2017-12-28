package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hhharsh on 28/10/17.
 */

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<Earth>> {

    private String mUrl;
    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    public EarthquakeLoader(Context context, String url) {
              super(context);
              mUrl = url;
           }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Earth> loadInBackground() {
        if (mUrl == null) {
                       return null;
                  }

                       // Perform the network request, parse the response, and extract a list of earthquakes.
               ArrayList<Earth> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
               return earthquakes;
    }


}
