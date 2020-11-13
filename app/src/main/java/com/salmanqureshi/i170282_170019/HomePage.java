package com.salmanqureshi.i170282_170019;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.security.auth.callback.Callback;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //NAVIGATION DRAWER VARIABLES START
    DrawerLayout drawerLayout;
    Integer REQUEST_CODE=1;
    NavigationView navigationView;
    Toolbar toolbar;
    RecyclerView chatRV;
    ImageView mainmenu;
    TextView text;
    ImageView profileImage,pIMG;
    List<String> msgs;
    //List<NewMessage> newMessages;
    List<ChatObject> MyChats;
    MyRvAdapter adapter;
    Bitmap image;
    MaterialButton searchserviceprovideronmapinput;
    FirebaseDatabase rootnode;
    DatabaseReference myref;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private StorageReference mStorageRef;
    Bitmap img;
    String name;
    String phone;
    String rcvId;
    //NAVIGATION DRAWER VARIABLES START

    ImageView searchicon;
    TextView searchtext;
    de.hdodenhof.circleimageview.CircleImageView profilehomepage;

    ImageView homepagelogo;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        MyChats=new ArrayList<>();
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        mainmenu=findViewById(R.id.mainmenu);
        pIMG = findViewById(R.id.profileaccountimage);
        profilehomepage=findViewById(R.id.profilehomepage);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        msgs = new ArrayList<>();
        Log.d("BC", mAuth.getUid());
        StorageReference pullImg = mStorageRef.child("ProfilePictures").child(mAuth.getUid());
        final long ONE_MEGABYTE = 1024 * 1024;
        pullImg.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                ImageView imgViewer = (ImageView) findViewById(R.id.profilehomepage);
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                imgViewer.setMinimumHeight(dm.heightPixels);
                imgViewer.setMinimumWidth(dm.widthPixels);
                //pIMG.setImageBitmap(bm);
                imgViewer.setImageBitmap(bm);
            }
        });
        chatRV=findViewById(R.id.chatRV);
        RecyclerView.LayoutManager lm= new LinearLayoutManager(this);
        chatRV.setLayoutManager(lm);
        adapter=new MyRvAdapter(MyChats,this);
        chatRV.setAdapter(adapter);
        getPermissions();
        getUserChat();

        //newMessages.add(new NewMessage(image,"Akash Ali","How are you?","12:13 PM",true,"1"));

    }

    @Override
    protected void onResume() {
        super.onResume();
        homepagelogo=findViewById(R.id.homepagelogo);
        homepagelogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, MessageActivity.class);
                startActivity(intent);
            }
        });


        searchicon=findViewById(R.id.searchicon);
        searchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomePage.this, SearchUser.class);
                startActivity(intent);
            }
        });
        searchtext=findViewById(R.id.searchtext);
        searchtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, SearchUser.class);
                startActivity(intent);
            }
        });
        profilehomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog( HomePage.this,R.style.BottomSheetDialogTheme);
                View bottomSheetView= LayoutInflater.from(HomePage.this).inflate(R.layout.layout_bottom_sheet,(LinearLayout)findViewById(R.id.bottomsheet_cl));
                bottomSheetView.findViewById(R.id.myeditprofile).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(HomePage.this, EditProfile.class);
                        startActivity(intent);
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

            }
        });
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);



        View header = navigationView.getHeaderView(0);
        text = (TextView) header.findViewById(R.id.username);
        profileImage=(ImageView) header.findViewById(R.id.circleImageView);
        mainmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        adapter.setOnItemClickListener(new MyRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String name = MyChats.get(position).getName();
                myref = FirebaseDatabase.getInstance().getReference().child("Users");
                Query getall = myref.orderByChild("phone").equalTo(MyChats.get(position).getPhone());
                getall.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String name = "",
                                    phno = "";
                            for(DataSnapshot childSnapshot : snapshot.getChildren()){
                                String key = childSnapshot.getKey();
                                if(!key.equals(mAuth.getInstance().getCurrentUser().getUid().toString())) {
                                      setData(childSnapshot.getKey(),position);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void setData(String toString,int position) {
        rcvId= toString;
        StartIt(name,rcvId,position);
    }

    private void StartIt(String name, String img,int position) {
        Intent intent=new Intent(HomePage.this,MessageActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("img",img);
        intent.putExtra("key",MyChats.get(position).getKey());
        startActivity(intent);
    }
    private void getUserChat(){
        DatabaseReference mUserChatDB = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("chat");
        mUserChatDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot childSnapshot : snapshot.getChildren()){
                        ChatObject chat = new ChatObject(childSnapshot.getKey());
                        DatabaseReference mprofile = FirebaseDatabase.getInstance().getReference().child("Users").child(childSnapshot.getValue().toString());
                        mprofile.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                collectData(snapshot,childSnapshot);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        //chat.SetOnline();
                        //   this will cause null exception Log.d("BC", chat.getMessage());
                        //boolean exists = false;
                        //for (ChatObject mItr : MyChats){
                            //if(mItr.getKey().equals(chat.getKey())){
                            //    exists = true;
                          //  }
                        //}
                        //if(exists){
                         //   continue;
                       // }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void collectData(DataSnapshot snapshot,DataSnapshot childSnapshot) {
        for (DataSnapshot child : snapshot.getChildren()){
            if(child.getKey().equals("fname")){
                name = child.getValue().toString();
            }
            if(child.getKey().equals("lname")){
                name = name + " ";
                name += child.getValue().toString();
            }
            if(child.getKey().equals("phone")){
                phone = child.getValue().toString();
            }
        }
        StorageReference pullImg = mStorageRef.child("ProfilePictures").child(childSnapshot.getValue().toString());
        final long ONE_MEGABYTE = 1024 * 1024;
        pullImg.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                img = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                update(name,img,childSnapshot.getKey(),phone);
            }
        });
    }

    private void update(String name, Bitmap img,String key,String phone) {

        DatabaseReference msg1 = FirebaseDatabase.getInstance().getReference().child("Chat").child(key);
        Query lastmsg = msg1.limitToLast(1);
        lastmsg.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child: snapshot.getChildren()){
                    anotherone(child.child("text").getValue().toString(),key,phone);
                }
            }

            @java.lang.Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //android.util.Log.d("BCCCCCCCC",(msgs.get(0)) );

}

    private void anotherone(String snapshot,String key,String phone) {
        MyChats.add(new ChatObject(img,name,snapshot,"12:13 PM",true,"1",key,phone));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }



    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_logout:
                //Intent intent=new Intent(BasicSearch.this,History.class);
                //startActivity(intent);
                break;

        }
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getPermissions() {
        requestPermissions(new String []{Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS},1);
    }
    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("BC", "Error getting bitmap", e);
        }
        return bm;
    }
}