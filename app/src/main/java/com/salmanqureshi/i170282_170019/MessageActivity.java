package com.salmanqureshi.i170282_170019;

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
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.core.provider.FontsContractCompat.FontRequestCallback.RESULT_OK;

public class MessageActivity extends AppCompatActivity {
    Integer REQUEST_CAMERA=1, SELECT_IMAGE=0;
    List<String>mediaUriList;
    String name,picS,ChatId;
    int total = 0;
    ArrayList<String> idList = new ArrayList<>();

    Bitmap img;
    EditText sendermessageedittext;
    ImageView sendmessageicon,mediabtn;
    ImageView message_back_arrow;
    TextView msgRcv;
    CircleImageView pic;
    private StorageReference mStorageRef;
    MessageAdapter messageAdapter;
    List<Chat> mchat;
    MessageAdapter adapter;
    MediaAdapter mAdapter;
    RecyclerView messageRV,mMedia;
    DatabaseReference mChatDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        msgRcv=findViewById(R.id.messagereceivername);
        pic = findViewById(R.id.message_receiver_image);
        mediabtn=findViewById(R.id.getimagefromcamera);
        name = getIntent().getStringExtra("name");
        picS = getIntent().getStringExtra("img");
        ChatId = getIntent().getStringExtra("key");
        mediaUriList = new ArrayList<>();
        mChatDb = FirebaseDatabase.getInstance().getReference().child("Chat").child(ChatId);
        mchat = new ArrayList<>();
        mediaUriList = new ArrayList<>();
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


        mMedia=findViewById(R.id.Media);
        mMedia.setHasFixedSize(true);
        RecyclerView.LayoutManager lM= new LinearLayoutManager(this);
        mMedia.setLayoutManager(lM);
        mAdapter=new MediaAdapter(mediaUriList,this);
        mMedia.setAdapter(mAdapter);

        sendermessageedittext=findViewById(R.id.sendermessageedittext);
        message_back_arrow= findViewById(R.id.message_back_arrow);

        mediabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });
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
                    ArrayList<String> mUriList = new ArrayList<>();
                    if (snapshot.child("text").getValue() != null) {
                        msg = snapshot.child("text").getValue().toString();
                    }
                    if (snapshot.child("creator").getValue() != null) {
                        sndr = snapshot.child("creator").getValue().toString();
                    }
                    if(snapshot.child("media").getChildrenCount()>0){
                        for(DataSnapshot uris : snapshot.child("media").getChildren()){
                            mUriList.add(uris.getValue().toString());
                        }
                    }
                    Chat Message = new Chat(img, sndr, snapshot.getKey(), msg,mUriList);
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
        total = 0;
        sendermessageedittext=findViewById(R.id.sendermessageedittext);
        if(!sendermessageedittext.getText().toString().isEmpty()){
            String mId = mChatDb.push().getKey();
            DatabaseReference mref = mChatDb.child(mId);

            Map mHash = new HashMap<>();
            mHash.put("text",sendermessageedittext.getText().toString());
            mHash.put("creator", FirebaseAuth.getInstance().getUid());

            //mref.updateChildren(mHash);


            if(!mediaUriList.isEmpty()){
                for(String mediaUri: mediaUriList){
                    String mediaId = mref.child("media").push().getKey();
                    idList.add(mediaId);
                    final StorageReference path = FirebaseStorage.getInstance().getReference().child("chat").child(ChatId).child(mId).child(mediaId);
                    UploadTask uptask = path.putFile(Uri.parse(mediaUri));
                    uptask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @java.lang.Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<android.net.Uri>() {
                                @java.lang.Override
                                public void onSuccess(android.net.Uri uri) {
                                    mHash.put("/media/"+idList.get(total) + "/",uri.toString());
                                    total++;
                                    if(total == mediaUriList.size()){
                                        updateDB(mref,mHash);
                                    }
                                }
                            });
                        }
                    });
                }
            }else{
                updateDB(mref,mHash);
            }
        }
        sendermessageedittext.setText(null);
    }

    private void updateDB(DatabaseReference mref,Map msg){
        mref.updateChildren(msg);
        sendermessageedittext.setText(null);
        mediaUriList.clear();
        idList.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MessageActivity.this,HomePage.class);
        startActivity(intent);

    }

    private void SelectImage() {
        final CharSequence[] items={"Camera","Gallery","Cancel"};
        AlertDialog.Builder builder=new AlertDialog.Builder(MessageActivity.this);
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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK)
        {
            if(requestCode==REQUEST_CAMERA)
            {
                Bitmap bitmap=(Bitmap)data.getExtras().get("data");
                Uri dp = getImageUri(MessageActivity.this,bitmap);
                mediaUriList.add(dp.toString());
            }
            else if(requestCode==SELECT_IMAGE)
            {
                Uri selectImage=(Uri) data.getData();
                mediaUriList.add(selectImage.toString());
            }
        }
        mAdapter.notifyDataSetChanged();
    }

}