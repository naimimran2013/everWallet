package com.everwallet.everwalletapp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Reset extends AppCompatActivity {

    private Button reset, back;
    private EditText inputEmail;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset);

        getSupportActionBar().setTitle("Settings");
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#333333")));

        reset = (Button) findViewById(R.id.reset_pass);
        back = (Button) findViewById(R.id.btn_back);
        inputEmail = (EditText) findViewById(R.id.reset_email);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetMail();
            }
        });



    }

    private void resetMail() {

        FirebaseAuth auth = FirebaseAuth.getInstance();



        String inputemail = inputEmail.getText().toString().trim();

        // String emailAddress = "xmlivenayeem@gmail.com";

        if (TextUtils.isEmpty(inputemail)) {
            Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(inputemail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Log.d(TAG, "Email sent.");
                            Toast.makeText(getApplicationContext(),"Reset password email is sent!", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Failed to send reset email!", Toast.LENGTH_LONG).show();
                        }

                        progressBar.setVisibility(View.GONE);
                        inputEmail.setText("");
                    }
                });
    }
}
