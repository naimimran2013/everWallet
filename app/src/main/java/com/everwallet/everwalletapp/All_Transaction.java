package com.everwallet.everwalletapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class All_Transaction extends AppCompatActivity {
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter;
    private FirebaseRecyclerOptions<Model> options;
    private TextView noAds;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__transaction);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("All Transaction");

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
        options = new FirebaseRecyclerOptions.Builder<Model>().setQuery(mRef, Model.class).build();
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

                       /* //get Data from firebase at the position click
                        String mTitle = getItem(position).getTitle();
                        String mDesc = getItem(position).getDescription();
                        String mPrice = getItem(position).getPrice();
                        String mSellerName = getItem(position).getSellerName();
                        String mLocation = getItem(position).getLocation();
                        String mBikeType = getItem(position).getBikeType();
                        String mBrand = getItem(position).getBrand();
                        String mModel = getItem(position).getModel();
                        String mModelYear = getItem(position).getModelYear();
                        String mCondition = getItem(position).getCondition();
                        String mEngineCapacity = getItem(position).getEngineCapacity();
                        String mKmRun = getItem(position).getKmRun();
                        String mPostTime = getItem(position).getPostTime();
                        String mImage = getItem(position).getImage();
                        String mImage2 = getItem(position).getImage2();
                        String mImage3 = getItem(position).getImage3();
                        String mImage4 = getItem(position).getImage4();
                        String mImage5 = getItem(position).getImage5();
                        String mPhone = getItem(position).getUserPhone();

                        //pass this data in new activity
                        Intent i = new Intent(view.getContext(), DetailsActivity.class);
                        i.putExtra("title", mTitle);
                        i.putExtra("description", mDesc);
                        i.putExtra("price", mPrice);
                        i.putExtra("sellerName", mSellerName);
                        i.putExtra("location", mLocation);
                        i.putExtra("bikeType", mBikeType);
                        i.putExtra("brand", mBrand);
                        i.putExtra("model", mModel);
                        i.putExtra("modelYear", mModelYear);
                        i.putExtra("condition", mCondition);
                        i.putExtra("engineCapacity", mEngineCapacity);
                        i.putExtra("kmRun", mKmRun);
                        i.putExtra("postTime", mPostTime);
                        i.putExtra("image", mImage);
                        i.putExtra("image2", mImage2);
                        i.putExtra("image3", mImage3);
                        i.putExtra("image4", mImage4);
                        i.putExtra("image5", mImage5);
                        i.putExtra("phone", mPhone);
                        startActivity(i);*/


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

        //set Layout at a GridLayout
        // mRecyclerView.setLayoutManager(mGridLayoutManager);

        //set Layout as a StageredGridLayout
        //mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        firebaseRecyclerAdapter.startListening();
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
