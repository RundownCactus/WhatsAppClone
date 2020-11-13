package com.salmanqureshi.i170282_170019;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchUser extends AppCompatActivity {
    RecyclerView userSearch;
    TextView text;
    ImageView profileImage;
    List<MyAppContact> users;
    UserSearchAdapter adapter;
    String rcvId;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener FirebaseAuthListener;
    private StorageReference mStorageRef;
    private FirebaseDatabase rootnode;
    private DatabaseReference myref;


    ImageView search_back_arrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        users=new ArrayList<>();
        userSearch=findViewById(R.id.usersRV);
        RecyclerView.LayoutManager lm= new LinearLayoutManager(this);
        userSearch.setLayoutManager(lm);
        adapter=new UserSearchAdapter(users,this);
        userSearch.setAdapter(adapter);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        getContactList();

        adapter.setOnItemClickListener(new MyRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                DatabaseReference mUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("chat");

                mUsers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for (DataSnapshot child : snapshot.getChildren()) {
                                if (child.getValue().toString().equals(users.get(position).getUid())) {
                                    String key = child.getKey();
                                    String name = users.get(position).getName();
                                    StartIt(name,rcvId,key);
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

    private void StartIt(String name, String img,String key) {
        Intent intent=new Intent(SearchUser.this,MessageActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("img",img);
        intent.putExtra("key",key);
        startActivity(intent);
    }

    private void getContactList(){
        Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while(phone.moveToNext()){
            String name = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phno = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            MyAppContact cont = new MyAppContact("",name,phno);
            getUserDetails(cont);
        }
    }

    private void getUserDetails(MyAppContact cont) {
        myref = FirebaseDatabase.getInstance().getReference().child("Users");
        Query getall = myref.orderByChild("phone").equalTo(cont.getPhone());
        getall.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                        String name = "",
                                phno = "";
                        for(DataSnapshot childSnapshot : snapshot.getChildren()){
                            String key = childSnapshot.getKey();
                            if(!key.equals(mAuth.getInstance().getCurrentUser().getUid().toString())) {

                                if (childSnapshot.child("phone").getValue() != null) {
                                    phno = childSnapshot.child("phone").getValue().toString();
                                }
                                if (childSnapshot.child("fname").getValue() != null) {
                                    name = childSnapshot.child("fname").getValue().toString() + " " +childSnapshot.child("lname").getValue().toString();
                                }
                                MyAppContact verUser = new MyAppContact(key,name, phno);
                                setData(childSnapshot.getKey());
                                users.add(verUser);
                                adapter.notifyDataSetChanged();
                                }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setData(String key) {
        rcvId = key;


    }


    @Override
    protected void onResume() {
        super.onResume();
        search_back_arrow=findViewById(R.id.search_back_arrow);
        search_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}