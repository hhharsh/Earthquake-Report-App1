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
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<ArrayList<Earth>> {

    private static final String JSON_RESPONSE = " https://earthquake.usgs.gov/fdsnws/event/1/query";

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    EarthAdapter adapter;
    private static final int EARTHQUAKE_LOADER_ID = 1;

    private TextView et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        et = (TextView) findViewById(R.id.empty_view);
        ListView lv = (ListView) findViewById(R.id.list);
        lv.setEmptyView(et);


        adapter = new EarthAdapter(this, new ArrayList<Earth>());
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        earthquakeListView.setAdapter(adapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Earth earthquake = adapter.getItem(position);

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(earthquake.get_url()));
                startActivity(i);


            }
        });
      /*  A obj = new A();
        obj.execute(JSON_RESPONSE);*/

/*        LoaderManager loaderManager = getLoaderManager();

        loaderManager.initLoader(EARTHQUAKE_LOADER_ID , null, this);*/


 /*   public void Updateui(ArrayList<Earth> earthquakes) {

    }*/
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if ((activeNetwork != null) && (activeNetwork.isConnected())) {

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }
        else{
            ProgressBar pb=(ProgressBar)findViewById(R.id.loading_indicator);
            pb.setVisibility(View.GONE);

            et.setText("Check Your Internet Connection");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<ArrayList<Earth>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
        Uri baseUri = Uri.parse(JSON_RESPONSE);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", "time");

        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Earth>> loader, ArrayList<Earth> earthquakes) {

        ProgressBar pb=(ProgressBar)findViewById(R.id.loading_indicator);
        pb.setVisibility(View.GONE);

        et.setText("No Earthquake Available");

        adapter.clear();

                        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
                               // data set. This will trigger the ListView to update.
                                        if (earthquakes != null && !earthquakes.isEmpty()) {
//                        mAdapter.addAll(earthquakes);
                                          adapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Earth>> loader) {
       adapter.clear();
    }


/*    private class A extends AsyncTask<String, Void, ArrayList<Earth>> {

        @Override
        protected ArrayList<Earth> doInBackground(String... params) {
            if (params.length < 1 || params[0] == null) {
                return null;
            }

            ArrayList<Earth> earthquakes = QueryUtils.fetchEarthquakeData(params[0]);

            return earthquakes;

        }

        @Override
        protected void onPostExecute(ArrayList<Earth> earthquakes) {
            Updateui(earthquakes);
        }

    }*/
}

