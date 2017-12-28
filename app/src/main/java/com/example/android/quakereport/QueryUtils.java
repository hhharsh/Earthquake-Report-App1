package com.example.android.quakereport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.android.quakereport.EarthquakeActivity.LOG_TAG;

/**
 * Created by hhharsh on 24/10/17.
 */

public class QueryUtils {


    public static ArrayList<Earth> fetchEarthquakeData(String requestUrl) {
        // Create URL object
       /* try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }*/
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        ArrayList<Earth> earthquake = extractFeatureFromJson(jsonResponse);

        // Return the {@link Event}
        return earthquake;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an {@link Earth} object by parsing out information
     * about the first earthquake from the input earthquakeJSON string.
     */
    private static ArrayList<Earth> extractFeatureFromJson(String earthquakeJSON) {

        ArrayList<Earth> earthquakes=new ArrayList<>();

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        try {

            JSONObject jobj = new JSONObject(earthquakeJSON);
            JSONArray jarr = jobj.getJSONArray("features");

            // If there are results in the features array
            for (int i = 0; i < jarr.length(); i++) {
                JSONObject jobj2 = jarr.getJSONObject(i);
                JSONObject jobj3 = jobj2.getJSONObject("properties");
                double aa = jobj3.getDouble("mag");
                String bb = jobj3.getString("place");
                String cc = jobj3.getString("time");

                String urlstr = jobj3.getString("url");

                Date dobj = new Date(Long.parseLong(cc));

                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                String dateToDisplay = dateFormatter.format(dobj);

                SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss a");
                String timeToDisplay = timeFormatter.format(dobj);

                earthquakes.add(new Earth(aa, bb, dateToDisplay, timeToDisplay, urlstr));
            }
            return earthquakes;
        }
            catch(JSONException e)

            {
                Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }
            return null;
        }
    }


