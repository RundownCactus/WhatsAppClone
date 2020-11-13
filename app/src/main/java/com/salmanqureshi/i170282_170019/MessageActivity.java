package com.salmanqureshi.i170282_170019;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    String name,picS,ChatId;
    Bitmap img;
    EditText sendermessageedittext;
    ImageView sendmessageicon;
    ImageView message_back_arrow;
    TextView msgRcv;
    CircleImageView pic;
    private StorageReference mStorageRef;
    MessageAdapter messageAdapter;
    List<Chat> mchat;
    MessageAdapter adapter;
    RecyclerView messageRV;
    DatabaseReference mChatDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        msgRcv=findViewById(R.id.messagereceivername);
        pic = findViewById(R.id.message_receiver_image);
        name = getIntent().getStringExtra("name");
        picS = getIntent().getStringExtra("img");
        ChatId = getIntent().getStringExtra("key");
        mChatDb = FirebaseDatabase.getInstance().getReference().child("Chat").child(ChatId);
        mchat = new ArrayList<>();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference pullImg = mStorageRef.child("ProfilePictures").child(picS);
        final long ONE_MEGABYTE = 1024 * 1024;
        pullImg.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                setImgs(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        });
        msgRcv.setText(name);
        messageRV=findViewById(R.id.messageRV);
        messageRV.setHasFixedSize(true);
        RecyclerView.LayoutManager lm= new LinearLayoutManager(this);
        messageRV.setLayoutManager(lm);
        adapter=new MessageAdapter(mchat,this);
        messageRV.setAdapter(adapter);
        sendermessageedittext=findViewById(R.id.sendermessageedittext);
        message_back_arrow= findViewById(R.id.message_back_arrow);
        message_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        sendmessageicon=findViewById(R.id.sendmessageicon);
        sendmessageicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=sendermessageedittext.getText().toString();
                if(!msg.equals(""))
                {
                    SendMessage();
                }
                else
                {
                    Toast.makeText(MessageActivity.this,"Can't send empty message.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        getMessages();
    }

    private void setImgs(Bitmap decodeByteArray) {
        pic.setImageBitmap(decodeByteArray);
        img = decodeByteArray;
    }

    private void getMessages() {
        mChatDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    String msg = "";
                    String sndr = "";

                    if (snapshot.child("text").getValue() != null) {
                        msg = snapshot.child("text").getValue().toString();
                    }
                    if (snapshot.child("creator").getValue() != null) {
                        sndr = snapshot.child("creator").getValue().toString();
                    }
                    Chat Message = new Chat(img, sndr, snapshot.getKey(), msg);
                    mchat.add(Message);
                    messageRV.getLayoutManager().scrollToPosition(mchat.size()-1);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void SendMessage() {
        sendermessageedittext=findViewById(R.id.sendermessageedittext);
        if(!sendermessageedittext.getText().toString().isEmpty()){
            DatabaseReference mref = mChatDb.push();

            Map mHash = new HashMap<>();
            mHash.put("text",sendermessageedittext.getText().toString());
            mHash.put("creator", FirebaseAuth.getInstance().getUid());

            mref.updateChildren(mHash);
        }
        sendermessageedittext.setText(null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MessageActivity.this,HomePage.class);
        startActivity(intent);

    }
}