package com.example.quang_tri.cartoonstream;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.drm.DrmStore;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.List;

import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity {
    private VideoView videoView;
    private int position = 0;
    private MediaController mediaController;
    private Button debugButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView = (VideoView) findViewById(R.id.videoView);
        debugButton = (Button) findViewById(R.id.debug);
        debugButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("com.example.quang_tri.cartoonstream.MyWebView");
                        //intent.setType("video/*");

                        //Need setDataAndType to be able to choose localcast as application
                        //intent.setDataAndType(Uri.parse("https://2.bp.blogspot.com/SDIvkU-vXTalmyFsWbfxNCoP9LmetMtHyQV2MY-XVMfIld95JNRfsAIi0LM82nJHT8Q8IbdTZ6d1VoximXhhO2MoPSr3Di8=m18"), "video/*");
                        //intent.setDataAndType(Uri.parse("http://playbb.me/embed.php?w=745&h=450&vid=at/c/chowder_2_-_17.mp4"), "video/mp4");
                        startActivity(intent);
                    }
                }
        );

        // Set the media controller buttons
        if (mediaController == null) {
            mediaController = new MediaController(MainActivity.this);

            // Set the videoView that acts as the anchor for the MediaController.
            mediaController.setAnchorView(videoView);


            // Set MediaController for VideoView
            videoView.setMediaController(mediaController);
        }


        try {
            // ID of video file.
            int id = this.getResources().getIdentifier("marblesoda", "raw", this.getPackageName());
            videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + id));

        } catch (Exception e) {
            e.printStackTrace();
        }

        videoView.requestFocus();


        // When the video file ready for playback.
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {


                videoView.seekTo(position);
                if (position == 0) {
                    //videoView.start();
                }

                // When video Screen change size.
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

                        // Re-Set the videoView that acts as the anchor for the MediaController
                        mediaController.setAnchorView(videoView);
                    }
                });
            }
        });

    }

    // When you change direction of phone, this method will be called.
    // It store the state of video (Current position)
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Store current position.
        savedInstanceState.putInt("CurrentPosition", videoView.getCurrentPosition());
        videoView.pause();
    }


    // After rotating the phone. This method is called.
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Get saved position.
        position = savedInstanceState.getInt("CurrentPosition");
        videoView.seekTo(position);
    }
}
