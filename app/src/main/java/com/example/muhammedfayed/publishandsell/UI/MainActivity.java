package com.example.muhammedfayed.publishandsell.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.muhammedfayed.publishandsell.R;
import com.example.muhammedfayed.publishandsell.UI.LoginActivity;
import com.example.muhammedfayed.publishandsell.adapters.CustomAdapter;
import com.example.muhammedfayed.publishandsell.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference mDatabase;
    private ArrayList<Post> mArrayList;
    private RecyclerView mRecyclerView;
    private CustomAdapter customAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isNetworkAvailable(getApplicationContext())) {
            checkLogin();
            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), OrientationHelper.VERTICAL, false);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            SwipeRefreshLayout swipeContainer;
            swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }

            });
        } else {
            finish();
            startActivity(new Intent(MainActivity.this,NoInternetActivity.class));
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        data();
    }

    public void data() {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mArrayList = new ArrayList<>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Post temp = new Post();
                    mArrayList.add(child.getValue(Post.class));
                }
                Collections.reverse(mArrayList);
                customAdapter = new CustomAdapter(mArrayList, getApplicationContext());
                mRecyclerView.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }


    public void checkLogin() {

        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        };
        auth.addAuthStateListener(authStateListener);


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                startActivity(new Intent(getApplicationContext(), AddNewPostActivity.class));
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("username");
                editor.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
