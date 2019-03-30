package com.project.hoangminh.clinicapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TimeZone;

public class Review extends AppCompatActivity {

    private RecyclerView reviewPanel;
    private LinearLayoutManager layoutMan;
    private ReviewAdapter rAdapter;
    private Button sendBtn;
    private FirebaseDatabase famCareDB;
    private DatabaseReference dbRef, timeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        reviewPanel = findViewById(R.id.review_panel);
        reviewPanel.setHasFixedSize(true);
        layoutMan = new LinearLayoutManager(this);
        reviewPanel.setLayoutManager(layoutMan);
        rAdapter = new ReviewAdapter();
        reviewPanel.setAdapter(rAdapter);

        ArrayList<String> vals = getIntent().getStringArrayListExtra("values");

        ArrayList<String> labels = new ArrayList<>(Arrays.asList("Heart Rate", "Respiratory Rate",
                "Blood Pressure", "Body Temperature", "Blood Oxygen", "Pain", "Sedation", "Awake",
                "Night Sleep", "Medical Compliance", "Intravenous", "Feeding Tube", "Appetite"));

        for(int i = 0; i < labels.size(); i++) {
            rAdapter.addItem(new ReviewItem(labels.get(i), vals.get(i)));
        }

        rAdapter.notifyDataSetChanged();

        //Read patient code from shared preferences
        SharedPreferences sp = this.getSharedPreferences(getString(R.string.PATIENT_CODE), Context.MODE_PRIVATE);
        String p_code = sp.getString(getString(R.string.PATIENT_CODE), "default");

        famCareDB = FirebaseDatabase.getInstance();
        dbRef = famCareDB.getReference().child(p_code).child("vitals");
        timeRef = famCareDB.getReference().child(p_code).child("time");

        sendBtn = findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LinkedHashMap<String,String> list = new LinkedHashMap<>();
//                for(ReviewItem r : rAdapter.getDataSet())
//                    list.put(r.getLabel(),r.getVal());
                dbRef.push().setValue(rAdapter.getDataSet());

                //Update time
                String[] months = {"January", "February", "March", "April", "May", "June",
                        "July", "August", "September", "October", "November", "December"};
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
                String time = "" + months[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.DAY_OF_MONTH) + ", " + cal.get(Calendar.YEAR)
                        + "  " + cal.get(Calendar.HOUR_OF_DAY) + " : " + cal.get(Calendar.MINUTE);
                timeRef.push().setValue(time);

                //Write to Shared Preferences, to let the app know that:
                //Data is not in the initial state anymore
                //New time has been posted
                writeToPref(R.string.PATIENT_TIME_KEY, 2, "", 1);

                Intent intent = new Intent(Review.this, UpdateVitals.class);
                startActivity(intent);
            }
        });

        Button cancelBtn = findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void writeToPref(int key, int code, String s, int i) {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        switch (code) {
            case 1:
                editor.putString(getString(key), s);
                break;
            case 2:
                editor.putInt(getString(key), i);
                break;
            default:
                break;
        }
        editor.apply();
    }
}
