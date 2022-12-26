package com.example.theislamicapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.UiModeManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

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
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends FragmentActivity {

    String user = "Error Please Log out";
    JSONObject reqJson;
    ArrayList<String> indexList = new ArrayList<>();
    Map<String, String> map = new TreeMap<>();

    ArrayAdapter<String> itemAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UiModeManager uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        uiModeManager.getCurrentModeType();
        Bundle extras = getIntent().getExtras();
        user = extras.getString("user");

        DatabaseReference database = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(user);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object object = dataSnapshot.getValue(Object.class);
                try {
                    reqJson = new JSONObject((new Gson().toJson(object)).trim());
                    if (check){
                        ck();
                    }else
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

        new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                log(String.valueOf(event));
                return false;
            }

            public boolean onKeyUp(View v, int keyCode, KeyEvent event) {
                log(String.valueOf(event));

                return false;
            }
        };
    }

    ListView lv;
    @SuppressLint("ResourceType")
    private void test(String valueObject) throws JSONException {
        JSONObject jsonObject = reqJson;
        boolean b = true;


        ArrayList<String> arr = new ArrayList<String>();

        if(valueObject!= null && valueObject.length()==0){
        }else {
            log2("doe");
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

        lv = findViewById(R.id.listviewQuran);
        itemAdapter = new ArrayAdapter<String>(this, R.layout.listviewitemquran,arr);
        lv.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                savedLastVaue = map.get(((TextView)view).getText());
                if(savedLastVaue.isEmpty()){
                    try {
                        indexList.add((String) ((TextView)view).getText());
                        test(savedLastVaue);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(savedLastVaue.substring(0,1).equals("-")) {
                    //link
                    Intent intent = new Intent(MainActivity.this, YoutubePlayer.class);
                    intent.putExtra("key", savedLastVaue.substring(1));
                    startActivity(intent);
                }else{
                    //folder;
                    savedIndex =(String) ((TextView)view).getText();
                    indexList.add((String) ((TextView)view).getText());
                    try {
                        test(savedLastVaue);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }//sdsadxzczc
                    //sdaq
                }
                log2(indexList.toString());
            }
        });
    }

    String savedLastVaue;
    String savedIndex;
    public void log(String str){
        if(str != null )
        Log.d("test123",str);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        log(String.valueOf(event));
        switch (keyCode) {


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
                    return true;
                }//jh
            }
            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_DPAD_UP:
                log(String.valueOf("up"));
                RelativeLayout rv = findViewById(R.id.menuid);
                RelativeLayout rv2 = findViewById(R.id.adminview);

                if(rv.getVisibility()==View.VISIBLE){
                    TextView logout = findViewById(R.id.textView);
                    TextView admin = findViewById(R.id.textView3);

                    if(logout.getCurrentTextColor()== Color.parseColor("#FF5722")){
                        admin.setTextColor(Color.parseColor("#FF5722"));
                        logout.setTextColor(Color.parseColor("#FFCDC7C7"));
                    }else{
                        logout.setTextColor(Color.parseColor("#FF5722"));
                        admin.setTextColor(Color.parseColor("#FFCDC7C7"));
                    }
                }
                else if(rv2.getVisibility() == View.VISIBLE){
                    RelativeLayout fodler = findViewById(R.id.addfolder);
                    ImageView link = findViewById(R.id.linkid);
                    Drawable dw  = fodler.getBackground();
                    if( fodler.getBackground().getConstantState().equals(getResources().getDrawable(R.color.backgroundorange).getConstantState()) ){
                        link.setBackground(getResources().getDrawable(R.color.backgroundorange));
                        fodler.setBackground(getResources().getDrawable(R.drawable.circle));
                    }else{
                        fodler.setBackground(getResources().getDrawable(R.color.backgroundorange));
                        link.setBackgroundColor(0);
                    }
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT :{
                RelativeLayout menu = findViewById(R.id.menuid);
                RelativeLayout admin = findViewById(R.id.adminview);
                if(menu.getVisibility() == View.VISIBLE){
                    menu.setVisibility(View.GONE);
                }
                if(admin.getVisibility() == View.VISIBLE){
                    admin.setVisibility(View.GONE);
                }
                return true;
            }
            case KeyEvent.KEYCODE_DPAD_RIGHT: {
                RelativeLayout menu = findViewById(R.id.menuid);
                RelativeLayout admin2 = findViewById(R.id.adminview);
                if(admin2.getVisibility() == View.VISIBLE){
                    RelativeLayout fodler = findViewById(R.id.addfolder);
                    ImageView link = findViewById(R.id.linkid);
                    if( fodler.getBackground().getConstantState().equals(getResources().getDrawable(R.color.backgroundorange).getConstantState()) ){
                        addFile(fodler);
                    }else{
                        addFile(link);
                    }
                }
                if(admin2.getVisibility() != View.VISIBLE) {
                    if (menu.getVisibility() != View.VISIBLE) {
                        menu.setVisibility(View.VISIBLE);
                        admin2.setVisibility(View.GONE);
                    } else {
                        TextView logout = findViewById(R.id.textView);
                        TextView admin = findViewById(R.id.textView3);
                        if (logout.getCurrentTextColor() == Color.parseColor("#FF5722")) {
                            logout(admin);
                        } else if (admin.getCurrentTextColor() == Color.parseColor("#FF5722")) {
                            adminShow(menu);
                        }
                    }
                }

                return true;
            }
            default:
                return super.onKeyUp(keyCode, event);
        }
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
                    String url =  (((TextView)dialog.findViewById(R.id.urlname)).getText()).toString();
                    String  name =  (((TextView)dialog.findViewById(R.id.urlname2)).getText()).toString();

                    Log.d("tag",name);

                    YoutubeLinkManagger lm = new YoutubeLinkManagger();
                    try {
                        lm.YoutubeLinkManaggert(MainActivity.this,indexList,url,user,name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();


                }
                check = true;

            }

        });
        dialog.show();
    }

    boolean check = false;
    public void showoptions(View view){

        RelativeLayout menu = findViewById(R.id.menuid);
        RelativeLayout admin2 = findViewById(R.id.adminview);

        if (admin2.getVisibility() == View.VISIBLE) {
            admin2.setVisibility(View.GONE);

        }
        if (menu.getVisibility() != View.VISIBLE) {
            menu.setVisibility(View.VISIBLE);

        } else {
            menu.setVisibility(View.GONE);

        }
    }

    public void ck(){
        ArrayList<String> arr2 = new ArrayList<String>();
        JSONObject jsonObject = new JSONObject();;
        try {
             jsonObject = new JSONObject(reqJson.toString().trim());

             for(String word: indexList){
                 jsonObject = new JSONObject(jsonObject.getString("*"+word).trim());
             }





            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                arr2.add(key.substring(1));

                String value = jsonObject.getString(key);
                map.put(key.substring(1), value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collections.sort(arr2, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return extractInt(o1) - extractInt(o2);
            }

            int extractInt(String s) {
                String num = s.replaceAll("\\D", "");
                // return 0 if no digits found
                return num.isEmpty() ? 0 : Integer.parseInt(num);
            }
        });

        lv = findViewById(R.id.listviewQuran);
        itemAdapter = new ArrayAdapter<String>(this, R.layout.listviewitemquran,arr2);
        lv.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();

    }

    public void log2(String str){
        Log.d("jh44",str);
    }
}

