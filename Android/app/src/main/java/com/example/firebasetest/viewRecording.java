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

        FaceDetectorOptions highAccuracyOpts =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .build();

        // Real-time contour detection
        FaceDetectorOptions realTimeOpts =
                new FaceDetectorOptions.Builder()
                        //.setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                        //.setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                        //.enableTracking()
                        .build();
        FaceDetector detector = FaceDetection.getClient(realTimeOpts);


        SparseArray<Face> faces;
        MediaMetadataRetriever mMMR = new MediaMetadataRetriever();
        mMMR.setDataSource(path);
        String timeMs = mMMR.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION); // video time in ms
        int totalVideoTime= 1000*Integer.valueOf(timeMs); // total video time, in uS
        for (int time_us=1;time_us<totalVideoTime;time_us+=deltaT){
            bitmap = mMMR.getFrameAtTime(time_us, MediaMetadataRetriever.OPTION_CLOSEST_SYNC); // extract a bitmap element from the closest key frame from the specified time_us
            if (bitmap==null) break;
            frame = new Frame.Builder().setBitmap(bitmap).build(); // generates a "Frame" object, which can be fed to a face detector
            //faces = detector.detect(frame); // detect the faces (detector is a FaceDetector)
            // TODO ... do something with "faces"
            InputImage image = InputImage.fromBitmap(bitmap, 0);
            try {
                savebitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


            Task<List<Face>> result =
                    detector.process(image)
                            .addOnSuccessListener(
                                    new OnSuccessListener<List<Face>>() {
                                        @Override
                                        public void onSuccess(List<Face> faces) {
                                            // Task completed successfully
                                            Log.d("tag1", "Face Detected");
                                            //Toast.makeText(viewRecording.this, "Face Detected", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                            Log.d("tag2", "Face Not Detected");

                                            //Toast.makeText(viewRecording.this, "Face Not Detected", Toast.LENGTH_SHORT).show();

                                        }
                                    });

        }

        final MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(v);
        Uri uri = Uri.parse(path);
        v.setMediaController(mediaController);
        v.setVideoURI(uri);
        v.requestFocus();
        v.start();
    }

    /** Create a File for saving an image or video */
    public static File savebitmap(Bitmap bmp) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "testimage.jpg");
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
        return f;
    }
//    private static void SaveImage(Bitmap finalBitmap) {
//
//        String root = Environment.getStorageDirectory().getAbsolutePath();
//        File myDir = new File(root + "/saved_images");
//        myDir.mkdirs();
//
//        String mTimeStamp = new SimpleDateFormat("ddMMyyyyHHmm").format(new Date());
//        String fname = "snap"+mTimeStamp+".jpg";
//
//        File file = new File (myDir, fname);
//        if (file.exists ()) file.delete ();
//        try {
//            FileOutputStream out = new FileOutputStream(file);
//            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
//            out.flush();
//            out.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}