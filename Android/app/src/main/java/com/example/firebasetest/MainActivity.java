package com.example.firebasetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText statusOfPIR, statusOfSystem, statusOfAlarm;
    Button arm, disarm, streamButton, start, stop;
    VideoView myVideoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        arm = (Button) findViewById(R.id.armBtn) ;
        disarm = (Button) findViewById(R.id.disarmBtn);
        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);


        statusOfPIR = (EditText) findViewById(R.id.motionStatus);
        statusOfSystem = (EditText) findViewById(R.id.systemStatus);
        statusOfAlarm = (EditText) findViewById(R.id.alarmStatus);
        //startActivity(new Intent(MainActivity.this,buzzer_screen_record.class));

        readDB();
    }

    public void footageList(View view){
        startActivity(new Intent(MainActivity.this,viewFootage.class));
    }

    public void screenRecord(View view){
        startActivity(new Intent(MainActivity.this,screen_record.class));
    }


    public void armWriteDB(View view){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference system = db.child("System");
        system.setValue("Armed");
    }

    public void disarmWriteDB(View view){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference system = db.child("System");
        system.setValue("Disarmed");
    }

    public void readDB(){
//        //write data
//        FirebaseDatabase db = FirebaseDatabase.getInstance();
//        DatabaseReference dr = db.getReference();
//        dr.child.("").setValue("");

        //read data
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference motion = db.child("Motion");
        DatabaseReference system = db.child("System");
        DatabaseReference alarm = db.child("Alarm");

        motion.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                Toast.makeText(MainActivity.this, "Data Received: "+value, Toast.LENGTH_LONG).show();
                statusOfPIR.setText("Motion: "+value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //failed to read value
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
                Log.d("H", "Failed to read value", error.toException());
            }
        });


        system.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                Toast.makeText(MainActivity.this, "Data Received: "+value, Toast.LENGTH_LONG).show();
                statusOfSystem.setText("System: "+value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
                Log.d("H", "Failed to read value", error.toException());
            }
        });


        alarm.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                Toast.makeText(MainActivity.this, "Data Received: "+value, Toast.LENGTH_LONG).show();
                statusOfAlarm.setText("Buzzer: "+value);
                if (value.equals("On")){
//                    Intent callIntent = new Intent(Intent.ACTION_CALL);
//                    callIntent.setData(Uri.parse("tel:5555215556"));//change the number
//                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
//                        return;
//                    }
                   startActivity(new Intent(MainActivity.this,buzzer_screen_record.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
                Log.d("H", "Failed to read value", error.toException());
            }
        });
    }

}