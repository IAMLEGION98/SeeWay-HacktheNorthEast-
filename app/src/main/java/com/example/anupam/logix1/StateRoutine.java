package com.example.anupam.logix1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rajanish on 3/24/18.
 */

public class StateRoutine extends AsyncTask<Object,String,String >
{
    public AsyncSResponse delegate  = null;
    String googlePlacesdata;
    GoogleMap mMap;
    String url;
    int count;
    String stateseen;
    float statetax;
    Activity mactivity;
    private ProgressDialog dialog;

    HashMap<String,Float> statetoll = new HashMap<String,Float>();
    StateRoutine(Activity activity )
    {
        mactivity = activity;
        delegate= (AsyncSResponse)activity;
        dialog= new ProgressDialog(activity);

        stateseen="";
        statetax=0;
        statetoll.put("Tamil Nadu",(float)150);
        statetoll.put("Kerala", (float) 400.0);
        statetoll.put("Maharashtra",(float)350);
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
    protected void onPreExecute() {

        //dialog.setMessage("Calculating State Tax.....");
      // dialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String,String>> routedata = null;
        StateParser parser = new StateParser();
        routedata = parser.parse(s);
        performout(routedata);
       delegate.processSFinish(stateseen);
        //if(dialog.isShowing())
        //dialog.dismiss();
        //super.onPostExecute(stateseen);
       // Log.d("The State availed Tax: ", String.valueOf(statetax));



    }


    private void performout(List<HashMap<String, String>> routedata) {



        for(int i=0;i<routedata.size();i++)
        {
            HashMap<String,String> routeplace = routedata.get(i);
            String state = routeplace.get("state");



                stateseen = state;

                if (count > 4) {
                    statetax += 350;
                }

                Log.d("States:", state);



        }
    }
}
