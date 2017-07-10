package com.example.muhammedfayed.publishandsell.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViewsService;

import com.example.muhammedfayed.publishandsell.OnGetDataListener;
import com.example.muhammedfayed.publishandsell.models.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class Wservice extends RemoteViewsService {

    public Wservice() {

    }


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new ListAdapter(this.getApplicationContext(), intent);
    }


}
