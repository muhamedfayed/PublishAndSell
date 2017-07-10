package com.example.muhammedfayed.publishandsell.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.muhammedfayed.publishandsell.R;
import com.example.muhammedfayed.publishandsell.models.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ListAdapter implements RemoteViewsService.RemoteViewsFactory, ValueEventListener {

    ArrayList<Post> mArraylist;
    private DatabaseReference mDatabase;

    Context mContext;


    int widget_Id;

    public ListAdapter(Context context, Intent intent) {
        this.mArraylist = new ArrayList<>();
        this.mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        this.mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mArraylist.clear();
        mDatabase.addValueEventListener(this);
        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        for (DataSnapshot child : dataSnapshot.getChildren()) {
            mArraylist.add(child.getValue(Post.class));
        }
        Collections.reverse(mArraylist);
        Log.i("arraylist size 1", String.valueOf(mArraylist.size()));
        synchronized (this) {
            this.notify();
        }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mArraylist.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView = new RemoteViews(mContext.getPackageName(), R.layout.post_4_widget);

        remoteView.setTextViewText(R.id.title, mArraylist.get(position).getTitle());


        return remoteView;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


}
