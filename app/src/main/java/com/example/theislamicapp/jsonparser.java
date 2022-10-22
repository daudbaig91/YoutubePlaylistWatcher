package com.example.theislamicapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class jsonparser {
    public HashMap JsonParser(String str, HashMap<String ,String> map) throws JSONException {
        JSONObject Jobject = new JSONObject(str);
        JSONArray Jarray = Jobject.getJSONArray("items");

        for (int i = 0; i < Jarray.length(); i++) {
            String id = ((JSONObject) Jarray.get(i)).getJSONObject("snippet")
                    .getJSONObject("resourceId").getString("videoId").toString();
            String title = ((JSONObject) Jarray.get(i)).getJSONObject("snippet").getString("title");
            map.put(id,title);
        }
        return map;
    }

    public String hasnextPage(JSONObject js) throws JSONException {
        String result = null;
        if(js.has("nextPageToken")){
            if(js.has("nextPageToken")){
                result = js.getString("nextPageToken") ;
            }
        }
        return result;
    }

}
