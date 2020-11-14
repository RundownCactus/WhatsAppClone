package com.salmanqureshi.i170282_170019;

import androidx.annotation.NonNull;
import androidx.core.view.LayoutInflaterCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import com.bumptech.glide.Glide;

import java.util.List;

import android.net.Uri;
import android.view.View;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {
    List<String> mediaList;
    Context context;

    public MediaAdapter(List<String> mediaList, Context context) {
        this.mediaList = mediaList;
        this.context = context;
    }

    @NonNull
    @java.lang.Override
    public MediaViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
        View layoutV = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media,null,false);
        MediaViewHolder mediaViewHolder = new MediaViewHolder(layoutV);
        return mediaViewHolder;
    }

    @java.lang.Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        Glide.with(context).load(Uri.parse(mediaList.get(position))).into(holder.media);
    }

    @java.lang.Override
    public int getItemCount() {
        return mediaList.size();
    }

    public class MediaViewHolder extends RecyclerView.ViewHolder{
        ImageView media;
        public MediaViewHolder(View itemView){
            super(itemView);
            media = itemView.findViewById(R.id.media);
        }
    }
}
