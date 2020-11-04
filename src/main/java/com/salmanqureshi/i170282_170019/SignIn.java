package com.salmanqureshi.i170282_170019;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class SignIn extends AppCompatActivity {
    TextView registerhere;
    TextInputEditText getemailaddress,getpassword;
    Button signinbutton1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        registerhere=findViewById(R.id.registerhere);
        registerhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignIn.this,Register.class);
                startActivity(intent);
            }
        });
        getemailaddress=findViewById(R.id.getemailaddress);
        getpassword=findViewById(R.id.getpassword);
        signinbutton1=findViewById(R.id.signinbutton1);
        signinbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}