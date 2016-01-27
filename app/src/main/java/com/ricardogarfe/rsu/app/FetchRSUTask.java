package com.ricardogarfe.rsu.app;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchRSUTask extends AsyncTask <String, Void, List<String>>{

    private final String LOG_TAG = FetchRSUTask.class.getSimpleName();

    private ArrayAdapter<String> mRsuAdapter;
    private final Context mContext;

    public FetchRSUTask(Context context, ArrayAdapter<String> rsuAdapter) {
        mContext = context;
        mRsuAdapter = rsuAdapter;
    }

    @Override
    protected List<String> doInBackground(String[] params) {
        // If there's no zip code, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }
        return getContainerDataFromAPI(params[0], params[1], params[2]);
    }

    /**
     * API call to retrieve data from container type and location.
     *
     * @param containerType
     * @param latitude
     * @param longitude
     * @return
     */
    private List<String> getContainerDataFromAPI(String containerType, String latitude, String longitude) {

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String rsuContainerJsonStr = null;

        String format = "json";
        String units = "metric";
        int numDays = 14;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            final String FORECAST_BASE_URL =
                    "http://mapas.valencia.es/lanzadera/gps/contenedores/";

            final String basicAuth = "Basic " + Base64.encodeToString(
                    "user:password".getBytes(), Base64.NO_WRAP);

            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendPath(containerType)
                    .appendPath(latitude)
                    .appendPath(longitude)
                    .build();

            URL url = new URL(builtUri.toString());

            // Create the request to OpenData API, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", basicAuth);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            rsuContainerJsonStr = buffer.toString();
            Log.i(LOG_TAG, "JSON Response:\t" + rsuContainerJsonStr);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getRSUContainerFromJson(rsuContainerJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        // This will only happen if there was an error getting or parsing data.
        return null;
    }

    /**
     * Parse JSON data from the server into String
     *
     * @param rsuContainerJsonStr
     * @return
     * @throws JSONException
     */
    private List<String> getRSUContainerFromJson(String rsuContainerJsonStr) throws JSONException {
        // These are the names of the JSON objects that need to be extracted.

        List<String> rsuContainerInfo = new ArrayList();

        // Container information
        final String RSU_TITTLE = "titulo";
        final String RSU_MESSAGE= "mensaje";
        final String RSU_DISTANCE = "distancia";
        final String RSU_LONDESTINY = "lonDestino";
        final String RSU_LATDESTINY = "latDestino";

        JSONArray rsuContainerJsonArray = new JSONArray(rsuContainerJsonStr);

        // Variables from rsu api response
        JSONObject rsuJsonObject;
        long latDestiny;
        long lonDestiny;
        int distance;
        String tittle;
        String message;

        for (int i = 0; i < rsuContainerJsonArray.length(); i++) {

            rsuJsonObject = rsuContainerJsonArray.getJSONObject(i);
            tittle = rsuJsonObject.getString(RSU_TITTLE);
            message = rsuJsonObject.getString(RSU_MESSAGE);
            distance = rsuJsonObject.getInt(RSU_DISTANCE);
            lonDestiny = rsuJsonObject.getInt(RSU_LONDESTINY);
            latDestiny = rsuJsonObject.getInt(RSU_LATDESTINY);

            rsuContainerInfo.add(message);

        }

        return rsuContainerInfo;
    }

    @Override
    protected void onPostExecute(List<String> result) {
        if (result != null && mRsuAdapter != null) {
            mRsuAdapter.clear();
            for(String rsuContainerStr : result) {
                mRsuAdapter.add(rsuContainerStr);
            }
        }
    }
}
