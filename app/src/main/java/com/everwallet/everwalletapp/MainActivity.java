package com.everwallet.everwalletapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.google.firebase.firestore.util.Util;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private TextView text, navName, navEmail, ntBuy, ntSell, skBuy, skSell, neteller, skrill, bkash, roket, dbbl;
    private EditText inputAmount;
    private Button btnSell, btnBuy;
    private Spinner dtype;
    String userName;
    String nt_buRate, nt_sellRate, sk_buRate, sk_sellRate, userEmail, userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        updateNameEmail();
        updateRate();
        updateReserve();

        dtype = (Spinner) findViewById(R.id.dtype);
        String[] doller_type = new String[]{"Neteller", "Skrill"};
        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, doller_type);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        dtype.setAdapter(spinnerArrayAdapter);

        inputAmount = (EditText) findViewById(R.id.inputAmount);
        btnSell = (Button) findViewById(R.id.btnSell);
        btnBuy = (Button) findViewById(R.id.btnBuy);
        ntBuy = (TextView) findViewById(R.id.ntBuy);
        ntSell = (TextView) findViewById(R.id.ntSell);
        skBuy = (TextView) findViewById(R.id.skBuy);
        skSell = (TextView) findViewById(R.id.skSell);

        neteller = (TextView) findViewById(R.id.neteller);
        skrill = (TextView) findViewById(R.id.skrill);
        bkash = (TextView) findViewById(R.id.bkash);
        roket = (TextView) findViewById(R.id.roket);
        text = (TextView) findViewById(R.id.text);
        text.setSelected(true);
        FloatingActionButton chat = findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    Intent sendIntent =new Intent("android.intent.action.MAIN");
                    sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.setType("text/plain");
                    sendIntent.putExtra(Intent.EXTRA_TEXT,"");
                    sendIntent.putExtra("jid", "+8801738340448" +"@s.whatsapp.net");
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                }
                catch(Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Error/n"+ e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {

                    int amount = Integer.parseInt(inputAmount.getText().toString());

                    String sellDtype = dtype.getSelectedItem().toString();

                    int nt = Integer.parseInt(nt_sellRate);
                    int sk = Integer.parseInt(sk_sellRate);

                    if (amount > 4) {
                        if (sellDtype.equals("Neteller")) {


                            int result = amount * nt;

                            Intent i = new Intent(MainActivity.this, Sell.class);
                            i.putExtra("Price", result);
                            i.putExtra("Doller_Type", sellDtype);
                            i.putExtra("Amount", amount);
                            startActivity(i);

                        } else if (sellDtype.equals("Skrill")) {


                            int result = amount * sk;

                            Intent i = new Intent(MainActivity.this, Sell.class);
                            i.putExtra("Price", result);
                            i.putExtra("Doller_Type", sellDtype);
                            i.putExtra("Amount", amount);
                            startActivity(i);

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter minimum amount 5$", Toast.LENGTH_LONG).show();

                    }


                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_LONG).show();

                }

                inputAmount.setText("");


            }


        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int amount = Integer.parseInt(inputAmount.getText().toString());


                String sellDtype = dtype.getSelectedItem().toString();

                int nt = Integer.parseInt(nt_buRate);
                int sk = Integer.parseInt(sk_buRate);


                if (sellDtype.equals("Neteller")) {


                    int result = amount * nt;
                    int vat = result * 2 / 100;
                    int finalprice = result + vat;


                    Intent i = new Intent(MainActivity.this, Buy.class);
                    i.putExtra("Price", finalprice);
                    i.putExtra("Vat", vat);
                    i.putExtra("Doller_Type", sellDtype);
                    i.putExtra("Amount", amount);
                    i.putExtra("UserName", userName);
                    startActivity(i);

                } else if (sellDtype.equals("Skrill")) {


                    int result = amount * sk;
                    int vat = result * 2 / 100;
                    int finalprice = result + vat;


                    Intent i = new Intent(MainActivity.this, Buy.class);
                    i.putExtra("Price", finalprice);
                    i.putExtra("Vat", vat);
                    i.putExtra("Doller_Type", sellDtype);
                    i.putExtra("Amount", amount);
                    i.putExtra("UserName", userName);
                    startActivity(i);

                }

                inputAmount.setText("");

            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setItemIconTintList(null);
        navigationView.getMenu().findItem(R.id.dashboard).getIcon().setColorFilter(getResources().getColor(R.color.navHomeClr), PorterDuff.Mode.SRC_IN);
        navigationView.getMenu().findItem(R.id.allTransection).getIcon().setColorFilter(getResources().getColor(R.color.navMyadsClr), PorterDuff.Mode.SRC_IN);
        navigationView.getMenu().findItem(R.id.myTransection).getIcon().setColorFilter(getResources().getColor(R.color.navMyaccClr), PorterDuff.Mode.SRC_IN);
        navigationView.getMenu().findItem(R.id.login).getIcon().setColorFilter(getResources().getColor(R.color.navPostClr), PorterDuff.Mode.SRC_IN);
        navigationView.getMenu().findItem(R.id.register).getIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        navigationView.getMenu().findItem(R.id.signuot).getIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);


        navName = (TextView) headerView.findViewById(R.id.navName);
        navEmail = (TextView) headerView.findViewById(R.id.navEmail);
        try {
            navEmail.setText(userEmail);
        } catch (Exception e) {

        }

        //load info
        //

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void updateNameEmail() {

        try {


            //get firebase auth instance
            auth = FirebaseAuth.getInstance();

            //get current user
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {


                //Update User Name
                final DatabaseReference UserName = FirebaseDatabase.getInstance().getReference("All_User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                DatabaseReference UserName1 = UserName.child("uname");
                UserName1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        userName = dataSnapshot.getValue(String.class);

                        navName.setText(userName);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Log.w(TAG, "onCancelled", databaseError.toException());

                    }
                });

            } else {
                navName.setText("EverWall");

            }

            userEmail = auth.getCurrentUser().getEmail();


        } catch (Exception e) {


        }
    }

    private void updateReserve() {


        //Roket Reserve
        final DatabaseReference res_roket = FirebaseDatabase.getInstance().getReference("reserve").child("Roket");
        res_roket.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String roket_blnc = dataSnapshot.getValue(String.class);

                if (roket != null) {
                    roket.setText(roket_blnc);

                } else {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.w(TAG, "onCancelled", databaseError.toException());

            }
        });

        //bKash Reserve
        final DatabaseReference res_bkash = FirebaseDatabase.getInstance().getReference("reserve").child("bKash");
        res_bkash.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String bkash_blnc = dataSnapshot.getValue(String.class);

                if (bkash != null) {
                    bkash.setText(bkash_blnc);

                } else {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.w(TAG, "onCancelled", databaseError.toException());

            }
        });

        //DBBL Reserve
        final DatabaseReference res_neteller = FirebaseDatabase.getInstance().getReference("reserve").child("neteller");
        res_neteller.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String neteller_blnc = dataSnapshot.getValue(String.class);

                if (neteller != null) {
                    neteller.setText(neteller_blnc);

                } else {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.w(TAG, "onCancelled", databaseError.toException());

            }
        });

        //Skrill Reserve
        final DatabaseReference res_skrill = FirebaseDatabase.getInstance().getReference("reserve").child("skrill");
        res_skrill.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String skrill_blnc = dataSnapshot.getValue(String.class);

                if (skrill != null) {
                    skrill.setText(skrill_blnc);

                } else {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.w(TAG, "onCancelled", databaseError.toException());

            }
        });


    }

    private void updateRate() {

        //Neteller Buy Rate
        DatabaseReference ntBuyRate = FirebaseDatabase.getInstance().getReference("update_Rate").child("neteller").child("buyRate");
        ntBuyRate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                nt_buRate = dataSnapshot.getValue(String.class);

                if (ntBuy != null) {
                    ntBuy.setText(nt_buRate);

                } else {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.w(TAG, "onCancelled", databaseError.toException());

            }
        });
        //Neteller Buy Rate


        //Neteller Sell Rate
        DatabaseReference ntSellRate = FirebaseDatabase.getInstance().getReference("update_Rate").child("neteller").child("sellRate");
        ntSellRate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                nt_sellRate = dataSnapshot.getValue(String.class);

                if (ntSell != null) {
                    ntSell.setText(nt_sellRate);

                } else {


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.w(TAG, "onCancelled", databaseError.toException());

            }
        });

        //Neteller Sell Rate


        //Skrill Buy Rate
        DatabaseReference skBuyRate = FirebaseDatabase.getInstance().getReference("update_Rate").child("skrill").child("buyRate");
        skBuyRate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                sk_buRate = dataSnapshot.getValue(String.class);

                if (skBuy != null) {
                    skBuy.setText(sk_buRate);

                } else {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.w(TAG, "onCancelled", databaseError.toException());

            }
        });
        //Skrill Buy Rate


        //Skrill Sell Rate
        DatabaseReference skSellRate = FirebaseDatabase.getInstance().getReference("update_Rate").child("skrill").child("sellRate");
        skSellRate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                sk_sellRate = dataSnapshot.getValue(String.class);

                if (skSell != null) {
                    skSell.setText(sk_sellRate);

                } else {


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.w(TAG, "onCancelled", databaseError.toException());

            }
        });

        //Skrill Sell Rate


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //
            super.onBackPressed();
            //super.onBackPressed();
        }
    }

    private void signOut() {
        auth.signOut();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*//noinspection SimplifiableIfStatement
        if (id == R.id.admin) {
            startActivity(new Intent(getApplicationContext(), Admin.class));


            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.dashboard) {
            // Handle the camera action
        } else if (id == R.id.allTransection) {
            startActivity(new Intent(getApplicationContext(), All_Transaction.class));

        } else if (id == R.id.myTransection) {
            startActivity(new Intent(getApplicationContext(), MyTransaction.class));

        } else if (id == R.id.login) {
            startActivity(new Intent(getApplicationContext(), Login.class));

        } else if (id == R.id.register) {
            startActivity(new Intent(getApplicationContext(), Register.class));

        } else if (id == R.id.signuot) {

            signOut();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
//        auth.addAuthStateListener(authListener);


    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
