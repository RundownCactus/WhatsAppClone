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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchUser extends AppCompatActivity {
    RecyclerView userSearch;
    TextView text;
    ImageView profileImage;
    List<MyAppContact> users,VerUsers;
    UserSearchAdapter adapter;
    Bitmap image;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener FirebaseAuthListener;

    private FirebaseDatabase rootnode;
    private DatabaseReference myref;


    ImageView search_back_arrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        image= BitmapFactory.decodeResource(getResources(),R.drawable.profile1);
        users=new ArrayList<>();
        VerUsers=new ArrayList<>();
        userSearch=findViewById(R.id.usersRV);
        RecyclerView.LayoutManager lm= new LinearLayoutManager(this);
        userSearch.setLayoutManager(lm);
        adapter=new UserSearchAdapter(users,this);
        userSearch.setAdapter(adapter);


        getContactList();
        adapter.setOnItemClickListener(new MyRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                
            }
        });

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
                                    name = childSnapshot.child("fname").getValue().toString() + childSnapshot.child("lname").getValue().toString();
                                }
                                MyAppContact verUser = new MyAppContact(childSnapshot.getKey(),name, phno);
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