package com.salmanqureshi.i170282_170019;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateProfile extends AppCompatActivity {
    MaterialButton savebutton;
    TextInputEditText fname,lname,dob,bio,phone;
    String gender;
    Chip male,female,nosay;
    String selectedChipData;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase rootnode;
    private DatabaseReference myref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        savebutton=findViewById(R.id.savebutton);
        fname = findViewById(R.id.getfirstname);
        lname = findViewById(R.id.getlastname);
        dob = findViewById(R.id.getdateofbirth);
        male = findViewById(R.id.male);
        phone = findViewById(R.id.getphonenumber);
        female = findViewById(R.id.female);
        nosay = findViewById(R.id.prefernottosay);
        bio = findViewById(R.id.getfullbio);


        CompoundButton.OnCheckedChangeListener checkedChangeListener= new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked)
                {
                    selectedChipData=compoundButton.getText().toString();
                    //Toast.makeText(ServiceProvidersListView.this,selectedChipData,Toast.LENGTH_SHORT).show();
                    getgender(selectedChipData);

                }
            }
        };
        male.setOnCheckedChangeListener(checkedChangeListener);
        female.setOnCheckedChangeListener(checkedChangeListener);
        nosay.setOnCheckedChangeListener(checkedChangeListener);

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Fname = fname.getText().toString();
                String Lname = lname.getText().toString();
                String Dob = dob.getText().toString();
                String Bio = bio.getText().toString();
                String Phone = phone.getText().toString();

                Profile User = new Profile(Fname,Lname,Dob,Phone,Bio,gender);
                rootnode = FirebaseDatabase.getInstance();
                myref = rootnode.getReference().child("Users").child(mAuth.getInstance().getCurrentUser().getUid());
                myref.setValue(User);

                Intent intent=new Intent(CreateProfile.this,HomePage.class);
                startActivity(intent);
            }
        });
    }

    private void getgender(String selectedChipData) {
        gender = selectedChipData;
    }
}