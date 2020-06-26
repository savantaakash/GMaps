package com.example.gmaps;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class GetNearbyplacesData extends AsyncTask<Object,String,String> {
    String googlePlacesData;
    GoogleMap googleMap;
    String url;

    @Override
    protected String doInBackground(Object... objects) {
        googleMap =  (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadUrls downloadUrls = new DownloadUrls();
        try{
          googlePlacesData = downloadUrls.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        try{
            JSONObject parentOnject= new JSONObject(s);
            JSONArray resultArray = parentOnject.getJSONArray("results");
           for (int i=0;i<resultArray.length();i++){
               JSONObject jsonObject = resultArray.getJSONObject(i);
               JSONObject locationobj = jsonObject.getJSONObject("geometry").getJSONObject("location");

               String latitude =locationobj.getString("lat");
               String longitude =locationobj.getString("lng");

               JSONObject nameObject =  resultArray.getJSONObject(i);
               String name = nameObject.getString("name");

               LatLng latLng = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
               MarkerOptions markerOptions = new MarkerOptions();
               markerOptions.title(name);
               markerOptions.position(latLng);

               googleMap.addMarker(markerOptions);





           }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
