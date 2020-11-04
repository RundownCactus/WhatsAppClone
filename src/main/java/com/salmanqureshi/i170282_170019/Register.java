package com.salmanqureshi.i170282_170019;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Register extends AppCompatActivity {
    TextView signinhere;
    Button registerbutton1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signinhere=findViewById(R.id.signinhere);
        signinhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Register.this,SignIn.class);
                startActivity(intent);
            }
        });
        registerbutton1=findViewById(R.id.registerbutton1);
        registerbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Register.this,CreateProfile.class);
                startActivity(intent);
            }
        });
    }
}