package com.salmanqureshi.i170282_170019;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.onesignal.OneSignal;

public class MainActivity extends AppCompatActivity {
    Button signinbutton,registerbutton;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener FirebaseAuthListener;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signinbutton=findViewById(R.id.signinbutton);
        Fresco.initialize(this);
        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SignIn.class);
                startActivity(intent);
            }
        });

        registerbutton=findViewById(R.id.registerbutton);
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Register.class);
                startActivity(intent);
            }
        });
        getPermissions();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getPermissions() {
        requestPermissions(new String []{Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS},1);
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(MainActivity.this, HomePage.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Log.d("BC", "onAuthStateChanged: ");
                    Intent intent = new Intent(MainActivity.this, HomePage.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };
    }
}