package com.example.theislamicapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

public class YoutubePlayer extends FragmentActivity {

    YouTubePlayerView youTubePlayerView;
    YouTubePlayer youtubePlayerSave;

    String keyYoutube = "UDvh63xHVa0";
    String pref = "qurantimes";
    Float currentSeconds;


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
            @Override
            public void onReady(@NonNull YouTubePlayer player) {
                Float duration = 0f;
                UrlsUrduOnly url = new UrlsUrduOnly();

                if ((getSharedPreferences(pref, MODE_PRIVATE)).contains(keyYoutube)){
                    duration = Float.valueOf(
                            getSharedPreferences(pref, MODE_PRIVATE)
                                    .getString(keyYoutube,""));
                }


                youtubePlayerSave = player;
                YouTubePlayerUtils.loadOrCueVideo(
                        player,
                        getLifecycle(),
                        keyYoutube,
                        duration
                );
            }
            @Override
            public void onCurrentSecond(@NotNull YouTubePlayer youTubePlayer, float v) {
                currentSeconds = v;
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
}
