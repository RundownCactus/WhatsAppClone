package com.salmanqureshi.i170282_170019;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    TextView signinhere;
    Button registerbutton1;
    TextInputEditText getemailaddress,getpassword,getpassword1;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener FirebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signinhere=findViewById(R.id.signinhere);
        getemailaddress = findViewById(R.id.getemailaddress1);
        getpassword1 = findViewById(R.id.getcreatepassword);
        getpassword = findViewById(R.id.getretypepassword);
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
                if(!validateEmail() | !validatePass()){
                    return;
                }
                String Useremail = getemailaddress.getText().toString();
                String Userpass = getpassword.getText().toString();

                mAuth.createUserWithEmailAndPassword(Useremail, Userpass).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(Register.this, "Sign Up Error", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("BHECNHOD", "SUCCES BHECNHIOD");
                            Toast.makeText(Register.this, "Sign Up SUCCESS", Toast.LENGTH_SHORT).show();
                            //String user_id = mAuth.getCurrentUser().getUid();
                            //DatabaseReference curr_user_db = FirebaseDatabase.getInstance().getReference().child("User").child("Customers").child(user_id);
                            //curr_user_db.setValue(true);
                        }
                    }
                });
                Intent intent=new Intent(Register.this,CreateProfile.class);
                startActivity(intent);
            }
        });

    }

    private boolean validatePass() {
        String emal = getpassword.getText().toString();
        String pass2 = getpassword1.getText().toString();


        if(!emal.matches(pass2) ){
            Toast.makeText(Register.this, "PASSWORD DO NOT MATCH & PASSWORD IS NOT STRONG ENOUGH", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!emal.matches(pass2)){
            Toast.makeText(Register.this, "PASSWORD DO NOT MATCH ", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            getemailaddress.setError(null);
            return true;
        }
    };

    private boolean validateEmail(){
        String emal = getemailaddress.getText().toString();
        String pattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(emal.isEmpty()){
            Toast.makeText(Register.this, "PLEASE ENTER A VALID EMAIL", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!emal.matches(pattern)){
            Toast.makeText(Register.this, "PLEASE ENTER A VALID EMAIL", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            getemailaddress.setError(null);
            return true;
        }
    };

}