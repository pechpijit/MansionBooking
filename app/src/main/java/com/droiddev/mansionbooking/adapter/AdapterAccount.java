package com.droiddev.mansionbooking.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.droiddev.mansionbooking.R;
import com.droiddev.mansionbooking.model.ModelPostHome;
import com.droiddev.mansionbooking.model.ModelUsrApartment;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;

public class AdapterAccount extends RecyclerView.Adapter<AdapterAccount.VersionViewHolder> {
    ArrayList<ModelUsrApartment> posts;
    String url;

    Context context;
    OnItemClickListener clickListener;

    public AdapterAccount(Activity applicationContext, ArrayList posts, String url) {
        this.context = applicationContext;
        this.posts = posts;
        this.url = url;
    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_account, viewGroup, false);
        return new VersionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VersionViewHolder versionViewHolder, final int i) {
        versionViewHolder.txt_name.setText(posts.get(i).getSystem());
//        versionViewHolder.txt_room.setText("ห้อง : " + posts.get(i).getRoom());
//        versionViewHolder.txt_build.setText("ตึก : " + posts.get(i).getBuild());

        try {
            Glide.with(context)
                    .load(url + "/images/build/" +posts.get(i).getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .error(R.drawable.nopic)
                    .into(versionViewHolder.img);
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {

        return posts.size();
    }

    class VersionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_name, txt_room, txt_build;
        ImageView img;

        public VersionViewHolder(View itemView) {
            super(itemView);

            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_room = (TextView) itemView.findViewById(R.id.txt_room);
            txt_build = (TextView) itemView.findViewById(R.id.txt_build);

            img = (ImageView) itemView.findViewById(R.id.image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getPosition());
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);

    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
}