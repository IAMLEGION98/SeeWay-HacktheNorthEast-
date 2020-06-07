package com.example.anupam.logix1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rajanish on 3/23/18.
 */

public class RouteParser {


    private HashMap<String,String> getPlace(JSONObject gPlaceJson)
    {
        HashMap<String,String> googlePlacemap= new HashMap<>();
        String distance = "-NA-";
        String duration = "-NA-";
        try {
            if(!gPlaceJson.isNull("status")) {

                distance = gPlaceJson.getJSONObject("distance").getString("text");
                duration = gPlaceJson.getJSONObject("duration").getString("text");

                googlePlacemap.put("distance", distance);
                googlePlacemap.put("duration", duration);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return googlePlacemap;
    }
    private List<HashMap<String,String>> getrinfo(JSONArray jsonArray)
    {
        int count = jsonArray.length();
        List<HashMap<String,String >> dataList = new ArrayList<>();
        HashMap<String,String> placemap = null;
        for(int i=0;i<count;i++)
        {
            try {
                placemap= getPlace((JSONObject)jsonArray.get(i));
                dataList.add(placemap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dataList;

    }
    public List<HashMap<String,String>> parse(String jsondata)
    {
        JSONArray jsonArray=null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsondata);
            jsonArray = jsonObject.getJSONArray("rows").getJSONObject(0).getJSONArray("elements");


        } catch (JSONException e) {
            e.printStackTrace();

        }
        System.out.println(jsonArray.length());
        return getrinfo(jsonArray);

    }

}
