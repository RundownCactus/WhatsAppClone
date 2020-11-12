package com.salmanqureshi.i170282_170019;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.MyViewHolder> {
    List<MyAppContact> newList;
    Context c;
    private MyRvAdapter.OnItemClickListener mListener;
    public UserSearchAdapter(List<MyAppContact> newList, Context c) {
        this.c=c;
        this.newList=newList;
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(MyRvAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
    @NonNull
    @Override
    public UserSearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemrow= LayoutInflater.from(c).inflate(R.layout.user_row,parent,false);
        return new UserSearchAdapter.MyViewHolder(itemrow,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserSearchAdapter.MyViewHolder holder, int position) {
        holder.username.setText(newList.get(position).getName());
        holder.userage.setText(newList.get(position).getPhone());

    }

    @Override
    public int getItemCount() {
        return newList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView userage;
        public MyViewHolder(@NonNull View itemView, final MyRvAdapter.OnItemClickListener listener) {
            super(itemView);
            username=itemView.findViewById(R.id.username);
            userage=itemView.findViewById(R.id.userage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);

                            DatabaseReference mUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("chat");
                            mUsers.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot child : snapshot.getChildren()) {
                                        if (!child.getValue().toString().equals(newList.get(position).getUid())) {
                                            String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("chat").child(key).setValue(newList.get(position).getUid());
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(newList.get(position).getUid()).child("chat").child(key).setValue(FirebaseAuth.getInstance().getUid());
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
            });
        }
    }
}
