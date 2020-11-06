package com.salmanqureshi.i170282_170019;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchUser extends AppCompatActivity {
    RecyclerView userSearch;
    TextView text;
    ImageView profileImage;
    List<MyAppContact> users;
    UserSearchAdapter adapter;
    Bitmap image;

    ImageView search_back_arrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        image= BitmapFactory.decodeResource(getResources(),R.drawable.profile1);
        users=new ArrayList<>();

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
        users.add(new MyAppContact(image,"Akash Ali","Male, ","25"));

        userSearch=findViewById(R.id.usersRV);
        RecyclerView.LayoutManager lm= new LinearLayoutManager(this);
        userSearch.setLayoutManager(lm);
        adapter=new UserSearchAdapter(users,this);
        userSearch.setAdapter(adapter);

        adapter.setOnItemClickListener(new MyRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
    }
}