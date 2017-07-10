package com.example.muhammedfayed.publishandsell.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.muhammedfayed.publishandsell.R;
import com.example.muhammedfayed.publishandsell.models.Post;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by muhammedfayed on 08/07/17.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private List<Post> postsList;
    private Context context;

    public CustomAdapter(List<Post> postsList, Context context) {
        this.postsList = postsList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Post temp = postsList.get(position);
        holder.title.setText(temp.getTitle());
        holder.des.setText(context.getText(R.string.description)+" : "+temp.getDes());
        holder.phone.setText(context.getText(R.string.phone)+" : "+temp.getPhone());
        holder.username.setText(temp.getUsername());
        holder.progressBar.setVisibility(View.VISIBLE);
        Picasso.with(context).load(temp.getImageUrl())
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                    }
                });

    }

    @Override
    public int getItemCount() {
        if (postsList != null)
            return postsList.size();
        else
            return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, des, phone, username;
        ImageView imageView;
        ProgressBar progressBar;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            des = (TextView) itemView.findViewById(R.id.des);
            phone = (TextView) itemView.findViewById(R.id.phone);
            username = (TextView) itemView.findViewById(R.id.username);
            imageView = (ImageView) itemView.findViewById(R.id.imageview);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);

        }
    }
}
