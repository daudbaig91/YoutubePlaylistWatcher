package com.example.theislamicapp;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DBmodifier {


    public void createdb(String str){
        DatabaseReference refdb = FirebaseDatabase.getInstance().getReference().child("Users");


        UrlsUrduOnly urls = new UrlsUrduOnly();
        urls.getQuran();
        urls.getQuranurduonly();
        urls.getQuranurdu();


        for(int i = 0; i < urls.urlsquranarr.length; i++) {
            refdb.child(str).child("*Quran - قرآن").child("-Surah " + String.valueOf(i+1)).setValue("-"+urls.urlsquranarr[i]);
            refdb.child(str).child("*Quran in Urdu - اردو میں قرآن").child("-Surah " + String.valueOf(i+1)).setValue("-"+urls.urlsquraurduhidarr[i]);
            refdb.child(str).child("*Quran Urdu Only - قرآن صرف اردو میں").child("-Surah " + String.valueOf(i+1)).setValue("-"+urls.urlsurduonlyidarr[i]);

        }
    }

    public void addfolder(String str, ArrayList<String> arr,String user){
        try {
            DatabaseReference tempRefDB = FirebaseDatabase.getInstance().getReference().child("Users").child(user);

            for (int i = 0; i < arr.size();i++){
                String child = "*"+ arr.get(i);
                tempRefDB = tempRefDB.child(child);

            }
            tempRefDB.child("*"+str).setValue("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addUrl(String str, ArrayList<String> arr,String user, String value){
        try {
            DatabaseReference tempRefDB = FirebaseDatabase.getInstance().getReference().child("Users").child(user);

            for (int i = 0; i < arr.size();i++){
                String child = "*"+ arr.get(i);
                tempRefDB = tempRefDB.child(child);

            }
            tempRefDB.child("-"+str).setValue("-"+value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
