package com.example.waste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText uName,userEmail, userContact, userPass;
    FirebaseAuth mAuth;
    Button button;
    ProgressBar progressBar;
    FirebaseFirestore firestore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        uName = (EditText) findViewById(R.id.userName);
        userEmail = (EditText)findViewById(R.id.userEmail);
        userPass = (EditText) findViewById(R.id.userCPass);
        userContact = (EditText) findViewById(R.id.userContact);
        button = (Button) findViewById(R.id.btn2);
        progressBar = (ProgressBar) findViewById(R.id.progresbar);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progresbar);

        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),registered_user.class));
            finish();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = userEmail.getText().toString().trim();
                final String password = userPass.getText().toString().trim();
                final String name = uName.getText().toString();
                final String contact = userContact.getText().toString();

                if (TextUtils.isEmpty(email)){
                    userEmail.setError("Email not entered");
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
                            userID = mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = firestore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("Fullname",name);
                            user.put("Email", email);
                            user.put("Password",password);
                            user.put("Contact",contact);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG,"onSuccess: User profile is created for "+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"onFailure: " +e.toString());
                                }
                            });
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
