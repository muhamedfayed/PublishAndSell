package com.example.muhammedfayed.publishandsell.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.muhammedfayed.publishandsell.R;
import com.example.muhammedfayed.publishandsell.UI.LoginActivity;
import com.example.muhammedfayed.publishandsell.UI.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by muhammedfayed on 30/06/17.
 */

public class Login extends Fragment {

    private Button loginBtn, createBtn;
    private EditText usernameE, passwordE;
    private FirebaseAuth auth;
    LinearLayout buttonsLayout;
    ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_fragment, container, false);

        initView(rootView);

        return rootView;
    }

    public void initView(View rootView) {

        auth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        buttonsLayout = (LinearLayout) rootView.findViewById(R.id.buttons_layout);
        createBtn = (Button) rootView.findViewById(R.id.register_btn);
        loginBtn = (Button) rootView.findViewById(R.id.login_btn);
        usernameE = (EditText) rootView.findViewById(R.id.username_edittext);
        passwordE = (EditText) rootView.findViewById(R.id.password_edittext);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.mViewPager.setCurrentItem(3, true);

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        final String username = usernameE.getText().toString().trim();
        String password = passwordE.getText().toString().trim();

        if (username.length() < 1) {
            usernameE.setError(getText(R.string.fill_fields));
        } else if (password.length() < 1) {
            passwordE.setError(getText(R.string.fill_fields));
        } else if (username.length() < 1 && password.length() < 1) {
            usernameE.setError(getText(R.string.fill_fields));
            passwordE.setError(getText(R.string.fill_fields));
        } else {
            buttonsLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            auth.signInWithEmailAndPassword(username + "@publishandsell.co", password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        startActivity(new Intent(getContext(), MainActivity.class));
                        Toast.makeText(getContext(), getText(R.string.loggedin), Toast.LENGTH_LONG).show();
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", username);
                        editor.commit();
                        getActivity().finish();
                    } else {
                        if (task.getException().getMessage().equals(getText(R.string.no_user))) {
                            usernameE.setError(getText(R.string.no_user_msg));
                        } else if (task.getException().getMessage().equals(getText(R.string.wrong_password))) {
                            passwordE.setError(getText(R.string.wrong_password_error));
                        } else {
                            Toast.makeText(getContext(), getText(R.string.problem_logging), Toast.LENGTH_LONG).show();
                        }
                        buttonsLayout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}
