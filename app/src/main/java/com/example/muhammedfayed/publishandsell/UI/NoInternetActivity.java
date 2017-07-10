package com.example.muhammedfayed.publishandsell.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.muhammedfayed.publishandsell.R;

public class NoInternetActivity extends AppCompatActivity {

    Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        mButton = (Button) findViewById(R.id.retry_btn);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(NoInternetActivity.this,MainActivity.class));
            }
        });
    }
}
