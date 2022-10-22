package com.example.theislamicapp;

import android.net.Uri;
import android.util.Log;
import android.webkit.URLUtil;

public class youtubeConverter {

    public String[] getId(String str){

        String[] arr = new String[2];


        Uri uri = Uri.parse(str);
        String paramValue = "Error";

        paramValue = uri.getQueryParameter("v");
        if(str.contains("v=") ){
            paramValue = uri.getQueryParameter("v");
            arr[0] = "v";
            arr[1] = paramValue;
        }else if(str.contains("/playlist?") ) {
            paramValue = uri.getQueryParameter("list");
            arr[0] = "list";
            arr[1] = paramValue;
        }
        else {
            Log.d("tsst",paramValue);
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
                return true;
            }
        }
        Log.d("validtest","not valid");
        return false;
    }
}
