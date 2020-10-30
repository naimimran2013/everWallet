package com.everwallet.everwalletapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Buy extends AppCompatActivity {

    private TextView dType, price, usdAmount, paymentNumber;
    private FirebaseAuth auth;
    private ImageButton copyBtn;
    private EditText userEmail, paymentLastDigit, PhoneNumber;
    private Button confirmBuyOrder;
    String bKashNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Buy");

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }

        dType = (TextView) findViewById(R.id.dType);
        price = (TextView) findViewById(R.id.price);
        usdAmount = findViewById(R.id.usdAmount);
        paymentNumber = findViewById(R.id.paymentNumber);
        paymentNumber.setText(bKashNumber);
        userEmail = findViewById(R.id.userEmailEt);
        PhoneNumber = findViewById(R.id.userPhoneEt);
        paymentLastDigit = findViewById(R.id.paymentLastDigit);
        confirmBuyOrder = findViewById(R.id.confirmBuyOrder);
        copyBtn = findViewById(R.id.copyBtn);

        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("CopyText", bKashNumber);
                clipboard.setPrimaryClip(clip);


                Toast.makeText(getApplicationContext(), "Number copied", Toast.LENGTH_LONG).show();
            }
        });

        //bkash Number
        DatabaseReference bkashnumber = FirebaseDatabase.getInstance().getReference("buysell").child("bkashNumber");
        bkashnumber.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                bKashNumber = dataSnapshot.getValue(String.class);
                paymentNumber.setText(bKashNumber);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

        Intent intent = getIntent();
        final int total_price = intent.getExtras().getInt("Price");
        final int vat = intent.getExtras().getInt("Vat");
        final String dollerType = intent.getStringExtra("Doller_Type");
        final int amount = intent.getExtras().getInt("Amount");
        final String userName = intent.getStringExtra("UserName");


        dType.setText(dollerType);
        price.setText(Integer.toString(total_price) + " Tk (Charge " + vat + " Tk)");
        usdAmount.setText(Integer.toString(amount) + " USD");

        confirmBuyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String buyDollerType = dollerType;
                String buyBdtAmount = String.format("%,d", total_price) + " Tk";
                String buyUsdAmount = Integer.toString(amount) + " USD";
                String buyBkashNumber = bKashNumber;
                String userSendingEmail = userEmail.getText().toString();
                String lastDigit = paymentLastDigit.getText().toString();
                String userPhone = PhoneNumber.getText().toString();
                String date = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault()).format(new Date());
                String status = "Processing";
                String orderType = "buy";
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String pushKey = FirebaseDatabase.getInstance().getReference("All_Order").push().getKey();

                if (TextUtils.isEmpty(userSendingEmail)) {
                    Toast.makeText(getApplicationContext(), "Enter your Neteller/Skrill Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(lastDigit)) {
                    Toast.makeText(getApplicationContext(), "Enter bKash/Rocket number last 4 digit", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(userPhone)) {
                    Toast.makeText(getApplicationContext(), "Enter phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                BuyInfo buyInfo = new BuyInfo(buyBkashNumber, buyBdtAmount, date, buyDollerType, lastDigit, orderType, pushKey,
                        userSendingEmail, status, buyUsdAmount, userID, userName, userPhone);
                FirebaseDatabase.getInstance().getReference("All_Order").push().setValue(buyInfo);
                FirebaseDatabase.getInstance().getReference("Buy_Order").push().setValue(buyInfo);
                FirebaseDatabase.getInstance().getReference("buyNotification").setValue(pushKey);



                Toast.makeText(getApplicationContext(), "Order placed", Toast.LENGTH_LONG).show();
                finish();

            }
        });
    }
}
