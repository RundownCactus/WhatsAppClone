package com.salmanqureshi.i170282_170019;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    EditText sendermessageedittext;
    ImageView sendmessageicon;
    ImageView message_back_arrow;
    TextView msgRcv;
    CircleImageView pic;
    private StorageReference mStorageRef;
    MessageAdapter messageAdapter;
    List<Chat> mchat;
    RecyclerView messageRV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        msgRcv=findViewById(R.id.messagereceivername);
        pic = findViewById(R.id.message_receiver_image);
        String name = getIntent().getStringExtra("name");
        String picS = getIntent().getStringExtra("img");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference pullImg = mStorageRef.child("ProfilePictures").child(picS);
        final long ONE_MEGABYTE = 1024 * 1024;
        pullImg.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                pic.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        });
        msgRcv.setText(name);
        messageRV=findViewById(R.id.messageRV);
        messageRV.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MessageActivity.this);
        linearLayoutManager.setStackFromEnd(true);
        messageRV.setLayoutManager(linearLayoutManager);


        message_back_arrow= findViewById(R.id.message_back_arrow);
        message_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        sendermessageedittext=findViewById(R.id.sendermessageedittext);
        sendmessageicon=findViewById(R.id.sendmessageicon);
        sendmessageicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=sendermessageedittext.getText().toString();
                if(!msg.equals(""))
                {

                }
                else
                {
                    Toast.makeText(MessageActivity.this,"Can't send empty message.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MessageActivity.this,HomePage.class);
        startActivity(intent);

    }
}