package com.example.theislamicapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class YoutubeLinkManagger {

    String keyApi = "AIzaSyCWlEH7YQV04DFQ4L0pHgkGX-U7N91gfa0";

    YoutubeLinkManagger(Context context,String name){

        youtubeConverter yc = new youtubeConverter();
        boolean bool = yc.isValidUrl("https://www.youtube.com/watch?v=J_EaZeFyHzd&t=1s");
        String[][] arr = null;
        if(bool) {
            try {
                arr = yc.getId(name);
            } catch (Exception e) {
                Log.d("lkjhkkj", e.toString());
            }

            if (arr[0][0].equals("v")) {

            } else if (arr[0][0].equals("list")) {


                okhttphandler http = new okhttphandler();

                HashMap<String, String> list = new HashMap<>();
                jsonparser parser = new jsonparser();
                String hasnext = "";
                String s = http.doInBackground2("https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=114&playlistId=PLjwQJGSaUpf80Z8XTS2EBQqyyl_HQux3f&key=" + keyApi);

                while (hasnext != null) {
                    try {
                        list = parser.JsonParser(s, list);
                        hasnext = parser.hasnextPage(new JSONObject(s));
                        if (hasnext != null) {
                            s = http.doInBackground2("https://www.googleapis.com/youtube/v3/playlistItems?pageToken=" + hasnext + "&part=snippet&maxResults=114&playlistId=PLjwQJGSaUpf80Z8XTS2EBQqyyl_HQux3f&key=" + keyApi);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else {
                Toast.makeText(context,"Invalid Format!",Toast.LENGTH_LONG).show();
            }
        }
    }
}
