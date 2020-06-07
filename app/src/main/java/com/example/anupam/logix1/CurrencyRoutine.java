package com.example.anupam.logix1;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class CurrencyRoutine extends AsyncTask<Object,String,String>
{
    public AsyncResponse delegate  = null;
    String url;
    String currencydata;
    String rate;
    Activity mactivity;
    CurrencyRoutine(Activity activity)
    {
        mactivity = activity;
        delegate= (AsyncResponse)activity;
    }
    @Override
    protected String doInBackground(Object... objects) {
        url = (String)objects[0];
        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            currencydata = downloadUrl.readurl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currencydata;
    }
    public void outdata(List<HashMap<String, String>> parsedata)
    {
        for(int i=0;i<parsedata.size();i++)
        {
            HashMap<String,String> routeplace = parsedata.get(i);
            rate = routeplace.get("cost");
            Log.d("The rate ",rate);



        }





    }


    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String,String>> routedata = null;
        CurrencyParser parser = new CurrencyParser();
        routedata = parser.parse(s);
        outdata(routedata);
        delegate.processFinish(rate);
    }
}
