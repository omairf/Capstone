package com.example.firebasetest;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;


import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//class used to record screen
//class allows users to view webserver stream from arduino esp8266 which has footage of arducam mini ov2640 2mp
public class screen_record extends AppCompatActivity {
    private static final int REQUEST_CODE = 1000;
    private static final int REQUEST_PERMISSION = 1001;
    private static final SparseIntArray ORIENTATION = new SparseIntArray();

    private MediaProjectionManager mMediaProjectionManager;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private MediaProjectionCallback mMediaProjectionCallback;
    private MediaRecorder mMediaRecorder;

    private int mScreenDensity;
    private static final int DISPLAY_WIDTH = 720;
    private static final int DISPLAY_HEIGHT = 1280;

    static {
        ORIENTATION.append(Surface.ROTATION_0, 90);
        ORIENTATION.append(Surface.ROTATION_90, 0);
        ORIENTATION.append(Surface.ROTATION_180, 270);
        ORIENTATION.append(Surface.ROTATION_270, 180);
    }
    WebView stream;

    private RelativeLayout mRootLayout;
    private ToggleButton mToggleButton;
    //private VideoView mVideoView;
    private String mVideoUrl = "";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_record);

        stream = (WebView) findViewById(R.id.stream);
        stream.loadUrl("http://192.168.2.34/stream");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenDensity = displayMetrics.densityDpi;

        mMediaRecorder = new MediaRecorder();
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        //mVideoView = findViewById(R.id.videoView);
        mToggleButton = findViewById(R.id.toggleButton);
        mRootLayout = findViewById(R.id.rootLayout);

        mToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(screen_record.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) +
                        ContextCompat.checkSelfPermission(screen_record.this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(screen_record.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(screen_record.this, Manifest.permission.RECORD_AUDIO)) {
                        mToggleButton.setChecked(false);
                        Snackbar.make(mRootLayout, "Permission", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Enable", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ActivityCompat.requestPermissions(screen_record.this,
                                                new String[] {
                                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                        Manifest.permission.RECORD_AUDIO
                                                },
                                                REQUEST_PERMISSION);
                                    }
                                });
                    } else {
                        ActivityCompat.requestPermissions(screen_record.this,
                                new String[] {
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.RECORD_AUDIO
                                },
                                REQUEST_PERMISSION);
                    }
                } else {
                    toggleScreenShare(v);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void toggleScreenShare(View v) {
        ToggleButton toggleButton = (ToggleButton) v;
        if (toggleButton.isChecked()) {
            Toast.makeText(this.getApplicationContext(),"Footage is now being recorded!", Toast.LENGTH_SHORT).show();
            initRecorder();
            reocrdScreen();
        } else {
            Toast.makeText(this.getApplicationContext(),"Recording has stopped!", Toast.LENGTH_SHORT).show();
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            stopRecordScreen();

//            mVideoView.setVisibility(View.VISIBLE);
//            mVideoView.setVideoURI(Uri.parse(mVideoUrl));
//            mVideoView.start();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void reocrdScreen() {
        if (mMediaProjection == null) {
            startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
            return;
        }

        mVirtualDisplay = createVirtualDisplay();
        mMediaRecorder.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private VirtualDisplay createVirtualDisplay() {
        return mMediaProjection.createVirtualDisplay("MainActivity", DISPLAY_WIDTH, DISPLAY_HEIGHT,
                mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mMediaRecorder.getSurface(),
                null, null);
    }

//    public String getCurSysDate() {
//        return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
//    }

//    public String writeFileExternalStorage() {
//
//        //Checking the availability state of the External Storage.
//        String state = Environment.getExternalStorageState();
//        if (!Environment.MEDIA_MOUNTED.equals(state)) {
//
//            //If it isn't mounted - we can't write into it.
//            return "Error";
//        }
//        String fileName = getCurSysDate();
//        //Create a new file that points to the root directory, with the given name:
//        File file = new File(getExternalFilesDir(null), fileName);
//
//
//        //This point and below is responsible for the write operation
//        return fileName;
//    }

    private void initRecorder() {
        try {
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

            mVideoUrl = new StringBuilder().append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)).append(new StringBuilder("/HomeSecurity").append(new SimpleDateFormat("dd-MM-yyy-hh_mm_ss")
                    .format(new Date())).append(".mp4").toString()).toString();

            mMediaRecorder.setOutputFile(mVideoUrl);
            mMediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setVideoEncodingBitRate(512*1000);
            mMediaRecorder.setVideoFrameRate(30);
            //mMediaRecorder.setOutputFile(writeFileExternalStorage());

            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            int orientation = ORIENTATION.get(rotation+90);
            mMediaRecorder.setOrientationHint(orientation);
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != REQUEST_CODE) {
            Toast.makeText(this, "Unk error", Toast.LENGTH_SHORT).show();
            return;
        }

        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            mToggleButton.setChecked(false);
            return;
        }

        mMediaProjectionCallback = new MediaProjectionCallback();
        mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
        mMediaProjection.registerCallback(mMediaProjectionCallback, null);
        mVirtualDisplay = createVirtualDisplay();
        mMediaRecorder.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private class MediaProjectionCallback extends MediaProjection.Callback {

        @Override
        public void onStop() {
            super.onStop();

            if (mToggleButton.isChecked()) {
                mToggleButton.setChecked(false);
                mMediaRecorder.stop();
                mMediaRecorder.reset();
            }

            mMediaProjection = null;
            stopRecordScreen();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void stopRecordScreen() {
        if (mVirtualDisplay == null)
            return;

        mVirtualDisplay.release();
        destroyMediaProjection();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void destroyMediaProjection() {
        if (mMediaProjection != null) {
            mMediaProjection.unregisterCallback(mMediaProjectionCallback);
            mMediaProjection.stop();
            mMediaProjection = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSION: {
                if (grantResults.length > 0 && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    toggleScreenShare(mToggleButton);
                } else {
                    mToggleButton.setChecked(false);
                    Snackbar.make(mRootLayout, "Permission", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Enable", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ActivityCompat.requestPermissions(screen_record.this,
                                            new String[] {
                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                    Manifest.permission.RECORD_AUDIO
                                            },
                                            REQUEST_PERMISSION);
                                }
                            });
                }
                return;
            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


}
