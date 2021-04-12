package com.example.firebasetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//class used to grab video from dcim and display it to user
public class viewRecording extends AppCompatActivity {

    VideoView v;
    String path;
    Bitmap bitmap;
    Frame frame;
    int deltaT = 66666;
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