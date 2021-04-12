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

import org.w3c.dom.Text;

//class used to register users into firebase authentication
public class Register extends AppCompatActivity {
    EditText email, password, confirmPassword;
    Button register, login;
    FirebaseAuth authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmpassword);
        register = (Button) findViewById(R.id.register);

        authentication = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String e = email.getText().toString();
                String pw = password.getText().toString();
                String confirmpw = confirmPassword.getText().toString();

                if(e.isEmpty()){
                    email.setError("Email is Required!");
                    email.requestFocus();
                }
                else if(pw.isEmpty()){
                    password.setError("Password Missing!");
                    password.requestFocus();
                }
                else if(confirmpw.isEmpty()){
                    confirmPassword.setError("You need to confirm your password!");
                    confirmPassword.requestFocus();
                }
                else if(e.isEmpty() && pw.isEmpty()){
                    Toast.makeText(Register.this, "Fields are empty!", Toast.LENGTH_LONG).show();

                }
                else if(!(e.isEmpty() && pw.isEmpty())){
                    authentication.createUserWithEmailAndPassword(e, pw).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(Register.this, "Sign up unsucessful!", Toast.LENGTH_LONG).show();
                            }
                            else
                                startActivity(new Intent(Register.this, Login.class));
                        }
                    });
                }
                else{
                    Toast.makeText(Register.this, "ERROR", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}