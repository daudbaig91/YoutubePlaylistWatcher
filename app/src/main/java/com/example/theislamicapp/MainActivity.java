package com.example.theislamicapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
 * Main Activity class that loads {@link MainFragment}.
 */
public class MainActivity extends FragmentActivity {

    String user = "Error Please Log out";
    JSONObject reqJson;
    ArrayList<String> indexList = new ArrayList<>();
    Map<String, String> map = new TreeMap<>();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        user = extras.getString("user");

        youtubeConverter yc = new youtubeConverter();



        DatabaseReference database = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(user);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object object = dataSnapshot.getValue(Object.class);
                try {
                    reqJson = new JSONObject((new Gson().toJson(object)).trim());
                    test(null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,"Error While Loading Cloud Database",Toast.LENGTH_LONG).show();
            }
        });

    }


    @SuppressLint("ResourceType")
    private void test(String valueObject) throws JSONException {
        JSONObject jsonObject = reqJson;
        boolean b = true;

        ArrayList<String> arr = new ArrayList<String>();

        if(valueObject!= null && valueObject.length()==0){
        }else {
            if(indexList.size()!=0){
                try {
                    jsonObject = new JSONObject(valueObject.trim());
                }catch (NullPointerException e){
                    b = false;
                }

            }
            if (b || (valueObject!= null && !valueObject.isEmpty())) {
                Iterator<String> keys = jsonObject.keys();

                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = jsonObject.getString(key);
                    map.put(key.substring(1), value);
                    arr.add(key.substring(1));
                }

                Collections.sort(arr, new Comparator<String>() {
                    public int compare(String o1, String o2) {
                        return extractInt(o1) - extractInt(o2);
                    }

                    int extractInt(String s) {
                        String num = s.replaceAll("\\D", "");
                        // return 0 if no digits found
                        return num.isEmpty() ? 0 : Integer.parseInt(num);
                    }
                });
            }

        }

        ListView lv = findViewById(R.id.listviewQuran);
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(this, R.layout.listviewitemquran,arr);
        lv.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = map.get(((TextView)view).getText());
                if(value.isEmpty()){
                    try {
                        indexList.add((String) ((TextView)view).getText());
                        test(value);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(value.substring(0,1).equals("-")) {
                    //link
                    Intent intent = new Intent(MainActivity.this, YoutubePlayer.class);
                    intent.putExtra("key", value.substring(1));
                    startActivity(intent);
                }else{
                    //folder;
                    indexList.add((String) ((TextView)view).getText());
                    try {
                        test(value);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void log(String str){
        Log.d("test123",str);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT :
            case KeyEvent.KEYCODE_DPAD_LEFT: {
                RelativeLayout menu = findViewById(R.id.menuid);
                if(menu.getVisibility() == View.VISIBLE){
                    menu.setVisibility(View.GONE);
                }else {
                    menu.setVisibility(View.VISIBLE);
                }

                return true;
            }
            case KeyEvent.KEYCODE_DPAD_DOWN: {
                RelativeLayout rv = findViewById(R.id.adminview);
                RelativeLayout rv2 = findViewById(R.id.menuid);
                if(rv2.getVisibility()==View.VISIBLE){
                    RelativeLayout bttn1 = findViewById(R.id.addfolder);
                    ImageView bttn2 = findViewById(R.id.linkid);
                   if(bttn1.getBackground().equals(Color.parseColor("#FFFFFF"))){
                       bttn2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                       bttn1.setBackgroundColor(0);
                   }else {
                       bttn1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                       bttn2.setBackgroundColor(0);
                   }
                }if(rv.getVisibility()==View.VISIBLE){

                }


            }
            case KeyEvent.KEYCODE_BACK: {
                if(indexList.size()==0) {
                    finishAffinity();
                }
                else{
                    indexList.remove(indexList.size()-1);
                    if(indexList.size()==0){
                        try {
                            Log.d("jsonObject.toString()", "menu");
                            test(null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        Log.d("jsonObject.toString()", String.valueOf(indexList.size()));
                        try {
                            JSONObject jsonObject = reqJson;
                            for (String str : indexList) {
                                jsonObject = jsonObject.getJSONObject("*"+str);
                                Log.d("jsonObject.toString()", jsonObject.toString());
                                test(jsonObject.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
                return true;
            }
        }
        return false;
    }


    public void adminShow(View view){
        RelativeLayout admin = findViewById(R.id.adminview);
        RelativeLayout menu = findViewById(R.id.menuid);
        if (admin.getVisibility() == View.VISIBLE){
            admin.setVisibility(View.INVISIBLE);
            menu.setVisibility(View.GONE);
        }else {
            admin.setVisibility(View.VISIBLE);
            menu.setVisibility(View.GONE);
        }
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    public void addFile(View view){

        Dialog dialog = new Dialog(MainActivity.this);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.fileadder);
        Button bttn = dialog.findViewById(R.id.buttonaddurl);

        if(view.getResources().getResourceName(view.getId()).contains("linkid")) {
            dialog.setContentView(R.layout.linkadder);
             bttn = dialog.findViewById(R.id.buttonaddurl2);
        }

        bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log("wrttie try");
                if(!view.getResources().getResourceName(view.getId()).contains("linkid")) {

                    String name = (((TextView) dialog.findViewById(R.id.urlname)).getText()).toString();
                    DBmodifier db = new DBmodifier();
                    db.addfolder(name, indexList,user);
                    dialog.dismiss();
                } else {
                    String name =  "test";
                    String  url=  "https://www.youtube.com/playlist?list=PLzGSkwYjvpIZznqM1fdwOEx4Vw8vSb6Ov";

                    YoutubeLinkManagger lm = new YoutubeLinkManagger();
                    lm.YoutubeLinkManaggert(MainActivity.this,indexList,url,user,name);




                    dialog.dismiss();

                }
            }
        });
        dialog.show();
    }


}

