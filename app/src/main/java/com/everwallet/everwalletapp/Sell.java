package com.everwallet.everwalletapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Sell extends AppCompatActivity {

    private TextView dType, price, payemntEmail, usdAmount;
    private EditText userEmailEt, paymentNumberEt, phoneNumberEt;
    private String pNetEmail, pSkEmail, dollerType, adminReceiveEmail;
    private ImageButton copyBtn;
    private Button confirmSellOrder;
    private Spinner bdtPMethod;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;


    String sellerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sell");

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {
            Intent i = new Intent(getApplicationContext(), Login.class);
            startActivity(i);

            finish();
        }

        //Update User Name
        final DatabaseReference UserName = FirebaseDatabase.getInstance().getReference("All_User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        DatabaseReference UserName1 = UserName.child("uname");
        UserName1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                sellerName = dataSnapshot.getValue(String.class);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });


        dType = (TextView) findViewById(R.id.dType);
        price = (TextView) findViewById(R.id.price);
        usdAmount = (TextView) findViewById(R.id.usdAmount);
        payemntEmail = (TextView) findViewById(R.id.paymentEmail);
        copyBtn = findViewById(R.id.copyBtn);
        bdtPMethod = findViewById(R.id.bdtPMethod);
        confirmSellOrder = findViewById(R.id.confirmsellOrder);
        userEmailEt = findViewById(R.id.userEmailEt);
        paymentNumberEt = findViewById(R.id.paymentNumberEt);
        phoneNumberEt = findViewById(R.id.userPhoneEt);


        final String[] doller_type = new String[]{"bKash personal", "bKash agent", "Rocket personal", "Rocket agent"};
        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, doller_type);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        bdtPMethod.setAdapter(spinnerArrayAdapter);


        Intent intent = getIntent();
        final int total_price = intent.getExtras().getInt("Price");
        final int amount = intent.getExtras().getInt("Amount");
        dollerType = intent.getStringExtra("Doller_Type");


        dType.setText(dollerType);
        price.setText(Integer.toString(total_price) + " Tk");
        usdAmount.setText(Integer.toString(amount) + " USD");

        if (dollerType.equals("Neteller")) {

            //net email
            DatabaseReference netmail = FirebaseDatabase.getInstance().getReference("buysell").child("netemail");
            netmail.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    final String netEmail = dataSnapshot.getValue(String.class);
                    payemntEmail.setText(netEmail);
                    adminReceiveEmail = netEmail;

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });


        } else if (dollerType.equals("Skrill")) {

            //skrill email
            DatabaseReference skmail = FirebaseDatabase.getInstance().getReference("buysell").child("skemail");
            skmail.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    final String skEmail = dataSnapshot.getValue(String.class);

                    payemntEmail.setText(skEmail);
                    adminReceiveEmail = skEmail;

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });


        }


        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("CopyText", adminReceiveEmail);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getApplicationContext(), "Email copied", Toast.LENGTH_LONG).show();
            }
        });


        confirmSellOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String sellDollerType = dollerType;
                String sellBdtAmount = String.format("%,d", total_price) + " Tk";
                String sellUsdAmount = Integer.toString(amount) + " USD";
                String sendingEmail = userEmailEt.getText().toString();
                String receivedEmail = adminReceiveEmail;
                String paymentNumber = paymentNumberEt.getText().toString();
                String paymentType = bdtPMethod.getSelectedItem().toString();
                String phoneNumber = phoneNumberEt.getText().toString();
                String userName = sellerName;
                String date = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault()).format(new Date());
                String status = "Processing";
                String orderType = "sell";
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String pushKey = FirebaseDatabase.getInstance().getReference("All_Order").push().getKey();


                if (TextUtils.isEmpty(sendingEmail)) {
                    Toast.makeText(getApplicationContext(), "Enter your Neteller/Skrill Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(paymentNumber)) {
                    Toast.makeText(getApplicationContext(), "Enter your bKash/Rocket number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(getApplicationContext(), "Enter phone number", Toast.LENGTH_SHORT).show();
                    return;
                }


                Sell_Info sell_info = new Sell_Info(sellDollerType, sellBdtAmount, sellUsdAmount, sendingEmail, receivedEmail, paymentNumber, paymentType, phoneNumber, userName, date, status, userId, pushKey, orderType);
                FirebaseDatabase.getInstance().getReference("All_Order").push().setValue(sell_info);
                FirebaseDatabase.getInstance().getReference("Sell_Order").push().setValue(sell_info);
                FirebaseDatabase.getInstance().getReference("sellNotification").setValue(pushKey);



                Toast.makeText(getApplicationContext(), "Order placed", Toast.LENGTH_LONG).show();
                finish();


            }
        });


    }

}
