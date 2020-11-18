package com.example.capstoneapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SecondActivity extends AppCompatActivity {
    Button ledON, ledOFF, ledON2, ledOFF2;
    TextView status, ipAddress;
    String web, ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        Bundle b = getIntent().getExtras();
        ip = b.getString("ipAddresss");

        ipAddress = (TextView) findViewById(R.id.ipAddressDisplay);
        status = (TextView) findViewById(R.id.status);
        ledON = (Button) findViewById(R.id.btnON1);
        ledOFF = (Button) findViewById(R.id.btnOFF1);
        ledON2 = (Button) findViewById(R.id.btnON2);
        ledOFF2 = (Button) findViewById(R.id.btnOFF2);


        ipAddress.setText(ip);

        ledON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                web = "/led1on";
                new action().execute();
            }
        });

        ledOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                web = "/led1off";
                new action().execute();
            }
        });

        ledON2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                web = "/led2on";
                new action().execute();
            }
        });

        ledOFF2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                web = "/led2off";
                new action().execute();
            }
        });
    }

    public class action extends AsyncTask<Void, Void, String> {
        public String doInBackground(Void... voids){
            try {
                Toast.makeText(SecondActivity.this, "Hello", Toast.LENGTH_LONG).show();
                URL url = new URL("https://"+ip+web);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream is = httpURLConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public void onPostExecute(String aVoid){
            super.onPostExecute(aVoid);
        }
    }
}
