package com.example.firebasetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText email, password;
    Button login, register;
    FirebaseAuth authentication;
    private FirebaseAuth.AuthStateListener authListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        authentication = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fireBaseUser = authentication.getCurrentUser();
                if(fireBaseUser != null){
                    Toast.makeText(Login.this, "You have logged in", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Login.this, Home.class);
                    startActivity(i);
                }
                else
                    Toast.makeText(Login.this, "Please Login", Toast.LENGTH_SHORT).show();

            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e = email.getText().toString();
                String pw = password.getText().toString();

                if(e.isEmpty()){
                    email.setError("Email is Required!");
                    email.requestFocus();
                }
                else if(pw.isEmpty()){
                    password.setError("Password Missing!");
                    password.requestFocus();
                }
                else if(e.isEmpty() && pw.isEmpty()){
                    Toast.makeText(Login.this, "Fields are empty!", Toast.LENGTH_LONG).show();

                }
                else if(!(e.isEmpty() && pw.isEmpty())){
                    authentication.signInWithEmailAndPassword(e, pw).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(Login.this, "Login Error, Please Login Again", Toast.LENGTH_LONG).show();

                            }
                            else if(e.equals("preet.khasakia@ontariotechu.net") ){
                                Intent goMain = new Intent(Login.this, MainActivity.class);
                                startActivity(goMain);
                            }
                            else{
                                Intent goHome = new Intent(Login.this, Home.class);
                                startActivity(goHome);
                            }
                        }
                    });
                }
                else
                    Toast.makeText(Login.this, "Error occured", Toast.LENGTH_LONG).show();

            }

        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));

            }
        });

    }
}