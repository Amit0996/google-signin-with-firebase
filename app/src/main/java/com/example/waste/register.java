package com.example.waste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class register extends AppCompatActivity {
    EditText uName,userEmail, userName, userContact, userPass;
    FirebaseAuth mAuth;
    Button button;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        uName = (EditText) findViewById(R.id.userName);
        userEmail = (EditText)findViewById(R.id.userEmail);
        userName = (EditText)findViewById(R.id.userName2);
        userPass = (EditText) findViewById(R.id.userCPass);
        userContact = (EditText) findViewById(R.id.userContact);
        button = (Button) findViewById(R.id.btn2);
        progressBar = (ProgressBar) findViewById(R.id.progresbar);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progresbar);

        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),registered_user.class));
            finish();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userName.getText().toString().trim();
                String password = userPass.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    userName.setError("Username not entered");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    userPass.setError("Password not entered");
                    return;
                }

                if (password.length()<6){
                    userPass.setError("Build a strong password");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                //register user in firebase.

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(register.this, "User created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),registered_user.class));
                        }else{
                            Toast.makeText(register.this, "Error  !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });


        }



    }
