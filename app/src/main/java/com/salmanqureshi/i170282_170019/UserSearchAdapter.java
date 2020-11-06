package com.salmanqureshi.i170282_170019;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.user_image.setImageBitmap(newList.get(position).getImage());
        holder.username.setText(newList.get(position).getName());
        holder.usergender.setText(newList.get(position).getGender());
        holder.userage.setText(newList.get(position).getAge());
    }

    @Override
    public int getItemCount() {
        return newList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView user_image;
        public TextView username;
        public TextView usergender;
        public TextView userage;
        public MyViewHolder(@NonNull View itemView, final MyRvAdapter.OnItemClickListener listener) {
            super(itemView);
            user_image=itemView.findViewById(R.id.user_image);
            username=itemView.findViewById(R.id.username);
            usergender=itemView.findViewById(R.id.usergender);
            userage=itemView.findViewById(R.id.userage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
