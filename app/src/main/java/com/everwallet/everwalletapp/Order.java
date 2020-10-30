package com.everwallet.everwalletapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Order extends AppCompatActivity {

    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter;
    private FirebaseRecyclerOptions<Model> options;
    private TextView noAds;
    private ProgressBar progressBar;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order);

        query = getIntent().getStringExtra("Query");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("Name"));


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        }, 3000);


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

        Query firebaseSearchQuery = mRef.orderByChild("orderType").startAt(query).endAt(query + "\uf8ff");

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

                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tran_item, parent, false);
                ViewHolder viewHolder = new ViewHolder(itemView);

                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        final String cPushKey = getItem(position).getPushKey();


                        AlertDialog alertDialog = new AlertDialog.Builder(Order.this)
                                //set icon
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                //set title
                                .setTitle("Order Status")
                                //set positive button
                                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        //set what would happen when positive button is clicked
                                        cancelDialog(cPushKey);
                                    }
                                })
                                //set negative button
                                .setNeutralButton("Processed", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        processed(cPushKey);
                                    }
                                })


                                .setNegativeButton("Processing", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        processing(cPushKey);

                                    }
                                })
                                .show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        final String cPushKey = getItem(position).getPushKey();

                        AlertDialog alertDialog = new AlertDialog.Builder(Order.this)
                                //set icon
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                //set title
                                .setTitle("Delete Order")
                                //set positive button
                                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        //set what would happen when positive button is clicked
                                        dialog.dismiss();
                                    }
                                })
                                //set negative button


                                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //Action for "Delete".
                                        Query mQuery = mRef.orderByChild("pushKey").equalTo(cPushKey);
                                        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                                    ds.getRef().removeValue();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();

                                            }
                                        });


                                    }
                                })
                                .show();


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

    private void processed(String cPushKey) {

        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mRef = mFirebaseDatabase.getReference("All_Order");

        Query query = mRef.orderByChild("pushKey").equalTo(cPushKey);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ds.getRef().child("status").setValue("Processed");


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void processing(String cPushKey) {
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mRef = mFirebaseDatabase.getReference("All_Order");

        Query query = mRef.orderByChild("pushKey").equalTo(cPushKey);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ds.getRef().child("status").setValue("Processing");


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
