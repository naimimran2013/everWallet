package com.everwallet.everwalletapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MyTransaction extends AppCompatActivity {

    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter;
    private FirebaseRecyclerOptions<Model> options;
    private TextView noAds;
    private ProgressBar progressBar;
    public String userID;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_transaction);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("My Transaction");


        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {


            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();


        }


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        }, 3000);

        try {

            userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        } catch (Exception e) {


        }

        noAds = findViewById(R.id.noAds);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        //send Query to Firebase Database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("All_Order");
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        showData();

    }

    private void showData() {

        String query = userID;

        Query firebaseSearchQuery = mRef.orderByChild("userId").startAt(query).endAt(query + "\uf8ff");

        options = new FirebaseRecyclerOptions.Builder<Model>().setQuery(firebaseSearchQuery, Model.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, Model model) {

                viewHolder.setDeatils(getApplicationContext(), model.getUserName(), model.getUsdAmount(), model.getDate(), model.getStatus());

                noAds.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);

            }


            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction, parent, false);
                ViewHolder viewHolder = new ViewHolder(itemView);

                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        final String cPushKey = getItem(position).getPushKey();


                        AlertDialog alertDialog = new AlertDialog.Builder(MyTransaction.this)
                                //set icon
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                //set title
                                .setTitle("Cancel Order")
                                //set message
                                .setMessage(getString(R.string.dialog))
                                //set positive button
                                .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        //set what would happen when positive button is clicked
                                        dialog.cancel();
                                    }
                                })
                                //set negative button


                                .setNegativeButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        cancelDialog(cPushKey);

                                    }
                                })
                                .show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        //do something
                    }
                });


                return viewHolder;
            }
        };


        //set Layout as a LinerLayout
        mRecyclerView.setLayoutManager(mLayoutManager);
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void cancelDialog(String cPushKey) {

        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mRef = mFirebaseDatabase.getReference("All_Order");

        Query query = mRef.orderByChild("pushKey").equalTo(cPushKey);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ds.getRef().child("status").setValue("Cancel");


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}