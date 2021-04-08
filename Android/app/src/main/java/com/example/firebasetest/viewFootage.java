package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class viewFootage extends AppCompatActivity {

    String pathOfVideo;
    ArrayList<String> names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_footage);
        names = new ArrayList<String>();

        final MediaController mediaController = new MediaController(this);
        final ListView list = findViewById(R.id.listView);

        retrieveList();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, names);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem=(String) list.getItemAtPosition(position);
                pathOfVideo = clickedItem;
                Intent i = new Intent(viewFootage.this, viewRecording.class);
                i.putExtra("pathOfVideos", pathOfVideo);
                startActivity(i);
                Toast.makeText(viewFootage.this,clickedItem,Toast.LENGTH_LONG).show();
            }
        });
//        checkList();
    }

    public void retrieveList(){
        File dcim = getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (dcim != null) {
            File[] pics = dcim.listFiles();
            if (pics != null) {
                for (File pic : pics) {
                    Log.d("helloOTF", "Image: " + pic.getName());
                    names.add(pic.getName());
                }
            }
        }
    }
//    public void checkList() {
//        for ( int j=0; j<names.size(); j++ )
//            Log.d("names", "Image: " + names.get(j));
//    }

}
