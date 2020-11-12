package com.example.capstoneapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Button ipSubmit;
    EditText ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipSubmit = (Button) findViewById(R.id.btnSubmit);
        ipAddress = (EditText) findViewById(R.id.ipAddress);
    }

    public void onSubmit(View view){
        Intent i = new Intent(this, SecondActivity.class);
        String ip = ipAddress.getText().toString();
        i.putExtra("ipAddresss", ip);
        startActivity(i);
    }
}