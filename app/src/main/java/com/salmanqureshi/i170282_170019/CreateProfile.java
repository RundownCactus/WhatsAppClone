package com.salmanqureshi.i170282_170019;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import static androidx.core.provider.FontsContractCompat.FontRequestCallback.RESULT_OK;

public class CreateProfile extends AppCompatActivity {
    Integer REQUEST_CAMERA=1, SELECT_IMAGE=0;
    ImageView accountimage;
    MaterialButton savebutton;
    TextInputEditText fname,lname,dob,bio,phone;
    Uri dp;
    String gender;
    Chip male,female,nosay;
    String selectedChipData;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase rootnode;
    private DatabaseReference myref;
    private StorageReference mStorageRef;

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
        mStorageRef = FirebaseStorage.getInstance().getReference();

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

        accountimage=findViewById(R.id.profilepic);
        accountimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });

    }

    private void SelectImage() {
        final CharSequence[] items={"Camera","Gallery","Cancel"};
        AlertDialog.Builder builder=new AlertDialog.Builder(CreateProfile.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(items[i].equals("Camera"))
                {
                    Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,REQUEST_CAMERA);
                }
                else if(items[i].equals("Gallery"))
                {
                    Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent,SELECT_IMAGE);

                } else if (items[i].equals("Cancel"))
                {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    private void getgender(String selectedChipData) {
        gender = selectedChipData;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK)
        {
            if(requestCode==REQUEST_CAMERA)
            {
                Bitmap bitmap=(Bitmap)data.getExtras().get("data");
                Uri dp = getImageUri(CreateProfile.this,bitmap);
                StorageReference pushImg = mStorageRef.child("ProfilePictures").child(mAuth.getUid());
                pushImg.putFile(dp).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("CREATE PROFILE ACTIVITY", "IMAGE UPLOADED");
                    }
                });
                accountimage.setImageURI(dp);
            }
            else if(requestCode==SELECT_IMAGE)
            {
                Uri selectImage=data.getData();
                StorageReference pushImg = mStorageRef.child("ProfilePictures").child(mAuth.getUid());
                pushImg.putFile(selectImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("CREATE PROFILE ACTIVITY", "IMAGE UPLOADED");
                    }
                });
                accountimage.setImageURI(selectImage);
                //accountimage.setVisibility(View.GONE);
            }
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}