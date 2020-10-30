package com.everwallet.everwalletapp;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ViewHolder extends RecyclerView.ViewHolder {

    View mView;
    Context context;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        //item Click

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });


        //item Long Click

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });
    }


    public void setDeatils(Context ctx, String userName, String usdAmount, String date, String status) {




        TextView tUserName = mView.findViewById(R.id.tUserName);
        TextView tAmount = mView.findViewById(R.id.tAmount);
        TextView tDate = mView.findViewById(R.id.tDate);
        TextView tStatus = mView.findViewById(R.id.tStatus);


        tUserName.setText("Username: " + userName);
        tAmount.setText("USD Amount: " + usdAmount);
        tDate.setText("Date :" + date);
        tStatus.setText("Status :" + status);






    }

    private ViewHolder.ClickListener mClickListener;


    public interface ClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);

    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener) {

        mClickListener = clickListener;

    }
}
