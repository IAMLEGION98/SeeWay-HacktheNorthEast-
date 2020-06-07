package com.example.anupam.logix1;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rajanish on 3/23/18.
 */

public class Dialoginfo extends AsyncTask<Object,String,String> {

    public AsyncResponse delegate  = null;
    String googlePlacesdata;
    GoogleMap mMap;
    String url;
    String distance;
    String duration;
    TextView tv1,tv2;
    Activity mactivity;

    public Dialoginfo(Activity activity)
    {
        mactivity = activity;
        delegate = (AsyncResponse) activity;
    }


    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];
        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googlePlacesdata =downloadUrl.readurl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googlePlacesdata;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String,String>> routedata = null;
        RouteParser parser = new RouteParser();
        routedata = parser.parse(s);
        performout(routedata);
        Log.d("distance ", distance);
        Log.d("duration", duration);
        tv1 = mactivity.findViewById(R.id.tvdistance);
        tv2=mactivity.findViewById(R.id.tvduration);
         delegate.processFinish(distance);
        tv1.setText(distance);
        tv2.setText(duration);


    }


    private void performout(List<HashMap<String, String>> routedata) {

        for(int i=0;i<routedata.size();i++)
        {
            HashMap<String,String> routeplace = routedata.get(i);
            distance = routeplace.get("distance");
            duration = routeplace.get("duration");

            System.out.println("The Duration "+duration);
            Log.d("distance ", distance);
            Log.d("duration", duration);


        }
    }
}
