package com.novigo.fiori.trackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;

import com.sendgrid.*;

public class choose_view extends AppCompatActivity {
    Button allbtn,filterbtn,loadbtn;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_view);
        startService(new Intent(this, ChildEventListener.class));
        allbtn = findViewById(R.id.allfobtn);
        filterbtn = findViewById(R.id.filterfobtn);
        progressBar = findViewById(R.id.pBar);
        loadbtn = findViewById(R.id.loadfo);
        progressBar.setVisibility(View.INVISIBLE);
        loadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                filterbtn.setEnabled(false);
                allbtn.setEnabled(false);
                Intent bottommap = new Intent(choose_view.this,bottomsheet_novigo_maps.class);
                startActivity(bottommap);

//                Handler handler = new Handler(Looper.getMainLooper());
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        sendMailUsingSendGrid("someone@example.com","badhusha.s@novigo.com","Body","Heading");
//                    }
//                });
//
//                Toast.makeText(choose_view.this,"Loading FOs from TM System",Toast.LENGTH_LONG).show();
            }
        });


        allbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(choose_view.this,RecyclerMain.class);
                intent.putExtra("SHIPPING_DATE","X");
                intent.putExtra("SHIPPING_LOCATION","X");
                startActivity(intent);
            }
        });

        filterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(choose_view.this,datefilter.class);
                startActivity(intent);
            }
        });
    }

    private void sendMailUsingSendGrid(String from, String to, String body, String heading) {
        Hashtable<String,String> hashtable = new Hashtable<>();
        hashtable.put("to",to);
        hashtable.put("from",from);
        hashtable.put("subject",body);
        hashtable.put("text",heading);

        SendGridAsyncTask email = new SendGridAsyncTask();
        try {
            email.execute(hashtable);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean isNetworkAvailable() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (UnknownHostException e) {
            // Log error
        }
        return false;

    }
}
