package com.example.theislamicapp;

import android.net.Uri;
import android.util.Log;
import android.webkit.URLUtil;

public class youtubeConverter {

    public String[][] getId(String str){

        String[][] arr = new String[1][1];


        Uri uri = Uri.parse(str);
        String paramValue = "Error";


        if(str.contains("v=") ){
            paramValue = uri.getQueryParameter("v");
            arr[0][0] = "v";
            arr[0][1] = paramValue;
        }else if(str.contains("/playlist?") ) {
            paramValue = uri.getQueryParameter("list");
            arr[0][0] = "list";
            arr[0][1] = paramValue;
        }
        else {
            arr[0] = null;
        }

        return arr;
    }

    public boolean isValidUrl(String url) {

        if (url == null) {
            return false;
        }
        if (URLUtil.isValidUrl(url)) {
            Uri uri = Uri.parse(url);
            if ("www.youtube.com".equals(uri.getHost())) {
                Log.d("validtest","valid");
                return true;
            }
        }
        Log.d("validtest","not valid");
        return false;
    }
}
