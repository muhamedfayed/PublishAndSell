package com.example.muhammedfayed.publishandsell.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.muhammedfayed.publishandsell.R;
import com.example.muhammedfayed.publishandsell.UI.LoginActivity;

/**
 * Created by muhammedfayed on 30/06/17.
 */

public class Intro extends Fragment {

    Button getstartedBtn;
    TextView login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.intro_fragment, container, false);

        initView(rootView);

        return rootView;
    }

    public void initView(View rootView){
        getstartedBtn = (Button) rootView.findViewById(R.id.getstarted_btn);
        login = (TextView) rootView.findViewById(R.id.login_txt);
        getstartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.mViewPager.setCurrentItem(2,true);

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.mViewPager.setCurrentItem(1,true);
            }
        });
    }
}
