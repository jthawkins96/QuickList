package com.example.johhawki.quicklist;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import java.net.URI;

public class VideoActivity extends AppCompatActivity {
    private VideoView video;
    private MediaController mc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        video=findViewById(R.id.video);

        //Getting the videourl from the intent
        Uri vurl = Uri.parse(getIntent().getStringExtra("VIDEO"));
        video.setVideoPath("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4");
        mc=new MediaController(this);
        mc.setAnchorView(video);
        video.setMediaController(mc);
        video.start();
    }


}
