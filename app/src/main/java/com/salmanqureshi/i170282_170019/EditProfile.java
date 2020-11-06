package com.salmanqureshi.i170282_170019;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class EditProfile extends AppCompatActivity {
    MaterialButton savebutton_ep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        savebutton_ep=findViewById(R.id.savebutton_ep);
        savebutton_ep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}