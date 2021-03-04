package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.MediaController;
import android.widget.VideoView;


public class viewRecording extends AppCompatActivity {

    VideoView v;
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recording);

        Intent intent = getIntent();
        String pathOfVideo = intent.getStringExtra("pathOfVideos");
        path = Environment.getExternalStorageDirectory().getPath()+"/DCIM/"+pathOfVideo;
        v = (VideoView) findViewById(R.id.replay);
        final MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(v);
        Uri uri = Uri.parse(path);
        v.setMediaController(mediaController);
        v.setVideoURI(uri);
        v.requestFocus();
        v.start();
    }
}