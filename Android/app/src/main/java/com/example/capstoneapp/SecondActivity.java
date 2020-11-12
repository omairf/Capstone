package com.example.capstoneapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

        status = (TextView) findViewById(R.id.status);
        ledON = (Button) findViewById(R.id.btnON1);
        ledOFF = (Button) findViewById(R.id.btnOFF1);
        ledON2 = (Button) findViewById(R.id.btnON2);
        ledOFF2 = (Button) findViewById(R.id.btnOFF2);
        ipAddress = (TextView) findViewById(R.id.ipAddressDisplay);

        ipAddress.setText(ip);
    }

    public void onClickHandler(View view) {
        switch (view.getId()) {
            case (R.id.btnON1):
                web = "/5/on";
                break;
            case (R.id.btnOFF1):
                web = "/5/off";
                break;
            case (R.id.btnON2):
                web = "/4/on";
                break;
            case (R.id.btnOFF2):
                web = "/4/off";
                break;
        }
        action a = new action();
        a.execute();
    }

    public class action extends AsyncTask<Void, Void, String> {
        public String doInBackground(Void... voids){
            try {
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
