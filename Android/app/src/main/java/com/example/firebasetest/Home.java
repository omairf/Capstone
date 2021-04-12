package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//home page used to enter mac address for each user account
public class Home extends AppCompatActivity {

    EditText m1, m2, m3, m4, m5, m6;
    Button addDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        m1 = (EditText) findViewById(R.id.m1);
        m2 = (EditText) findViewById(R.id.m2);
        m3 = (EditText) findViewById(R.id.m3);
        m4 = (EditText) findViewById(R.id.m4);
        m5 = (EditText) findViewById(R.id.m5);
        m6 = (EditText) findViewById(R.id.m6);

        addDevice = (Button) findViewById(R.id.addDevice);


        addDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mac1 = m1.getText().toString();
                String mac2 = m2.getText().toString();
                String mac3 = m3.getText().toString();
                String mac4 = m4.getText().toString();
                String mac5 = m5.getText().toString();
                String mac6 = m6.getText().toString();

                String wholeMac = mac1+mac2+mac3+mac4+mac5+mac6;

                if (wholeMac.equals("D8F15B111310")) {
                    Toast.makeText(Home.this, "This device is already taken", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Home.this, "This device does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



}