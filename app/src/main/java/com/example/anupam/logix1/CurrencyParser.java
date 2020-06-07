package com.example.anupam.logix1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CurrencyParser {
    private HashMap<String,String> getCurrency(JSONObject currencyJson)
    {
        HashMap<String,String> currencyMap = new HashMap<>();
        String currency = "";
        try {


            currency = currencyJson.getString("Cost");


            currencyMap.put("cost", currency);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return currencyMap;

    }
    private List<HashMap<String,String>> getData(JSONArray jsonArray)
    {

        List<HashMap<String,String >> dataList = new ArrayList<>();

        if (jsonArray != null) {
            int count = jsonArray.length();

            HashMap<String, String> placemap = null;

            try {
                placemap = getCurrency((JSONObject) jsonArray.get(0));
                dataList.add(placemap);
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
        return dataList;
    }
    public List<HashMap<String,String>> parse(String jsondata)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsondata);
            jsonArray = jsonObject.getJSONArray("Sheet1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getData(jsonArray);
    }
}
