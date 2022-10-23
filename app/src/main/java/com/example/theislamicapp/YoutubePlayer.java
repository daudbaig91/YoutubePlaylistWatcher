package com.example.theislamicapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicReference;

public class YoutubePlayer extends FragmentActivity {

    YouTubePlayerView youTubePlayerView;
    YouTubePlayer youtubePlayerSave;

    String keyYoutube = "UDvh63xHVa0";
    String pref = "qurantimes";
    Float currentSeconds;
    YouTubePlayer player2 = null;

    String status = "PLAYING";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            keyYoutube = extras.getString("key");
        }

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.initialize(new AbstractYouTubePlayerListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onReady(@NonNull YouTubePlayer player) {
                Float duration = 0f;
                UrlsUrduOnly url = new UrlsUrduOnly();

                if ((getSharedPreferences(pref, MODE_PRIVATE)).contains(keyYoutube)){
                    duration = Float.valueOf(
                            getSharedPreferences(pref, MODE_PRIVATE)
                                    .getString(keyYoutube,""));
                }

                player2 = player;
                youtubePlayerSave = player;
                YouTubePlayerUtils.loadOrCueVideo(
                        player,
                        getLifecycle(),
                        keyYoutube,
                        duration
                );
                //youtubePlayerSave.seekTo(20f);



            }
            @Override
            public void onCurrentSecond(@NotNull YouTubePlayer youTubePlayer, float v) {
                currentSeconds = v;
            }

            @Override
            public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state){
                Log.d("stringsa",state.toString());
                status = state.toString();
            }

        });


    }



    @Override
    protected void onRestart() {
        SavedContent();
        super.onRestart();
    }
    @Override
    protected void onDestroy() {
        SavedContent();
        super.onDestroy();
    }
    @Override
    protected void onPause() {

        SavedContent();
        super.onPause();
    }
    @Override
    protected void onStop() {
        SavedContent();
        super.onStop();
    }

    public void SavedContent(){
        Log.d("tstsaved","saved");
        if(currentSeconds >= 2.0) {
            SharedPreferences sharedPreferences = getSharedPreferences(pref, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(String.valueOf(keyYoutube), String.valueOf(currentSeconds - 2.0));
            editor.apply();
        }

    }
int i = 0;

    @SuppressLint("RestrictedApi")
    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        Log.d("string121", String.valueOf(i++));
        if (event.getKeyCode()== KeyEvent.KEYCODE_DPAD_CENTER ||event.getKeyCode()== KeyEvent.KEYCODE_DPAD_UP) {
            if (status.equals("PLAYING")) {
                player2.pause();
            } else {
                player2.play();
            }
        }
        else if (event.getKeyCode()== KeyEvent.KEYCODE_DPAD_LEFT)
                player2.seekTo(currentSeconds-=7);

        else if (event.getKeyCode()== KeyEvent.KEYCODE_DPAD_RIGHT)
                player2.seekTo(currentSeconds+=7);
        else if (event.getKeyCode()== KeyEvent.KEYCODE_BACK){
            finish();
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void run(View view){


    }
}
