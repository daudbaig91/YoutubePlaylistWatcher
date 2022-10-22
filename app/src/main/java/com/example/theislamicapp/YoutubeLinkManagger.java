package com.example.theislamicapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class YoutubeLinkManagger {

    String keyApi = "AIzaSyCWlEH7YQV04DFQ4L0pHgkGX-U7N91gfa0";

    public HashMap<String, String>  YoutubeLinkManaggert(Context context, ArrayList<String> indexList,String url, String user, String name){
        HashMap<String, String>  map = new HashMap<>();
        youtubeConverter yc = new youtubeConverter();
        boolean bool = yc.isValidUrl(url);
        String[] arr = new String[2];
        if(bool) {
            try {
                arr = yc.getId(url);
            } catch (Exception e) {
                Log.d("lkjhkkj", e.toString());
            }

            if (arr[0].equals("v")) {
                DBmodifier db = new DBmodifier();
                db.addUrl(name, indexList,user,arr[1]);

            } else if (arr[0].equals("list")) {
                map = getData(arr[1]);
                HashMap<String, String> finalMap = map;
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {


                    for (Map.Entry<String, String> entry : finalMap.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        DBmodifier db = new DBmodifier();
                        db.addUrl(value, indexList,user,key);
                    }
                    }
                });
            }else {
                Toast.makeText(context,"Invalid Format!",Toast.LENGTH_LONG).show();
            }
        }
        return map;
    }

    private HashMap<String, String> getData(String url){

        okhttphandler http = new okhttphandler();

        HashMap<String, String> list = new HashMap<>();
        jsonparser parser = new jsonparser();
        String hasnext = "";
        String s = http.doInBackground2("https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=114&playlistId=" + url +"&key=" + keyApi);

        while (hasnext != null) {
            try {
                list = parser.JsonParser(s, list);
                hasnext = parser.hasnextPage(new JSONObject(s));
                if (hasnext != null) {
                    s = http.doInBackground2("https://www.googleapis.com/youtube/v3/playlistItems?pageToken=" + hasnext + "&part=snippet&maxResults=114&playlistId=" + url +"&key=" + keyApi);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return list;
    }
}
