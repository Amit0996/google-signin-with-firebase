package com.example.waste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    AutoCompleteTextView auto1, auto2;
    ScrollView scrollView;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    FirebaseAuth mAuth;
    Button btn;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            scrollView.setVisibility(View.VISIBLE);
            textView1.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
            textView3.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawableResource(R.drawable.app_background);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        auto1 = (AutoCompleteTextView) findViewById(R.id.UserName);
        auto2 = (AutoCompleteTextView) findViewById(R.id.UserPass);
        scrollView = (ScrollView) findViewById(R.id.scrollView1);
        textView1 = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        btn = (Button) findViewById(R.id.btn);
        mAuth = FirebaseAuth.getInstance();

        handler.postDelayed(runnable, 1000); //splash time

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = auto1.getText().toString().trim();
                String password = auto2.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    auto1.setError("Username not entered");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    auto2.setError("Password not entered");
                    return;
                }

                if (password.length()<6){
                    auto2.setError("Build a strong password");
                    return;
                }

                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "welcome", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),dashboard.class));
                        }else{
                            Toast.makeText(MainActivity.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                    }
                });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),register.class));
            }
        });

        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),forgot.class));
            }
        });
    }

}

