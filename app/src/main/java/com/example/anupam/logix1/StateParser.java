package com.example.anupam.logix1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rajanish on 3/24/18.
 */

public class StateParser {

    private HashMap<String,String> getPlace(JSONObject gPlaceJson)
    {
        HashMap<String,String> googlePlacemap= new HashMap<>();
        String state = "";

        try {


            state = gPlaceJson.getString("long_name");


            googlePlacemap.put("state", state);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return googlePlacemap;
    }

    private List<HashMap<String,String>> getrinfo(JSONArray jsonArray) {
        List<HashMap<String, String>> dataList = new ArrayList<>();
        if (jsonArray != null) {
            int count = jsonArray.length();

            HashMap<String, String> placemap = null;

                try {
                    placemap = getPlace((JSONObject) jsonArray.get(3));
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
            jsonArray = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("address_components");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getrinfo(jsonArray);

    }
}
