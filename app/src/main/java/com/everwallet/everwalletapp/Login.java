package com.everwallet.everwalletapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    //private SharedPreferences sharedPreferences;
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "PrefsFile";
    private SharedPreferences.Editor editor;
    private CheckBox savelogincheckbox;
    private Boolean savelogin;
    String activityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        getSupportActionBar().setTitle("Sign in");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#333333")));


        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.pass);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        savelogincheckbox = (CheckBox) findViewById(R.id.remember);

        activityName = getIntent().getStringExtra("Acitvity");

        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        getPreferenceData();



        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {


            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();


        }


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login.this, Register.class));

            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login.this, Reset.class));

            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                loginDataSave();


                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(Login.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                    inputEmail.getText().clear();
                                    inputPassword.getText().clear();
                                    finish();
                                }
                            }
                        });

            }
        });


    }

    private void loginDataSave() {

        if (savelogincheckbox.isChecked()) {

            Boolean boolIsChecked = savelogincheckbox.isChecked();
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString("pref_email", inputEmail.getText().toString());
            editor.putString("pref_password", inputPassword.getText().toString());
            editor.putBoolean("pref_check", boolIsChecked);
            editor.apply();


        } else {
            mPrefs.edit().clear().apply();
        }


    }

    private void getPreferenceData() {

        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (sp.contains("pref_email")) {
            String e = sp.getString("pref_email", "Not Found");
            inputEmail.setText(e.toString());
        }
        if (sp.contains("pref_password")) {
            String p = sp.getString("pref_password", "Not Found");
            inputPassword.setText(p.toString());
        }
        if (sp.contains("pref_check")) {
            Boolean b = sp.getBoolean("pref_check", false);
            savelogincheckbox.setChecked(b);
        }
    }


}