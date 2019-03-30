package com.project.hoangminh.clinicapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.graphics.Typeface.BOLD;

public class PatientMain extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private FirebaseDatabase famCareDB;
    private DatabaseReference dbRef;
    private ChildEventListener timeListener;
    private TextView timeView;
    private Boolean timeUpdated = false;
    private final String DEFAULT_NAME = "Patient not yet set";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);

        TextView title = (TextView) findViewById(R.id.title_patientMain);
        SpannableString span = new SpannableString("FAMMCARE");
        span.setSpan(new RelativeSizeSpan(0.7f), 3, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new StyleSpan(BOLD), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        title.setText(span);

        ImageView backBtn = (ImageView) findViewById(R.id.backBtn_patientMain);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateBack();
            }
        });

        Button vitalsBtn = (Button) findViewById(R.id.vitalButton);
        vitalsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patientName = readNameFromPref();
                if (patientName.equals(DEFAULT_NAME)) {
                    Intent intent = new Intent(PatientMain.this, EmptyWarning.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(PatientMain.this, UpdateVitals.class);
                    startActivity(intent);
                }
            }
        });

        Button msgBtn = findViewById(R.id.msgButton);
        msgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patientName = readNameFromPref();
                if (patientName.equals(DEFAULT_NAME)) {
                    Intent intent = new Intent(PatientMain.this, EmptyWarning.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(PatientMain.this, SendMessages.class);
                    startActivity(intent);
                }
            }
        });

        Button noteBtn = findViewById(R.id.noteButton);
        noteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String patientName = readNameFromPref();
                if (patientName.equals(DEFAULT_NAME)) {
                    Intent intent = new Intent(PatientMain.this, EmptyWarning.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(PatientMain.this, WriteNote.class);
                    startActivity(intent);
                }
            }
        });

        timeView = findViewById(R.id.lastUpdate);

        ImageView options = findViewById(R.id.optionBtn_patientMain);
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(PatientMain.this, v);
                popup.setOnMenuItemClickListener(PatientMain.this);
                popup.inflate(R.menu.hamburger_menu);
                popup.show();
            }
        });

        //Read patient code from Shared Preferences
        SharedPreferences sp = this.getSharedPreferences(getString(R.string.PATIENT_CODE), Context.MODE_PRIVATE);
        String p_code = sp.getString(getString(R.string.PATIENT_CODE), "default");

        famCareDB = FirebaseDatabase.getInstance();
        dbRef = famCareDB.getReference().child(p_code).child("time");
        timeListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String t = dataSnapshot.getValue(String.class);
                if(!timeUpdated)
                    timeView.setText("Not updated yet");
                else
                    timeView.setText("Last updated on " + t);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };
        dbRef.addChildEventListener(timeListener);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent i = new Intent(PatientMain.this, Settings.class);
                startActivity(i);
                return true;
            case R.id.signout:
                navigateBack();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        navigateBack();
    }

    //Need to do this to keep the state of the parent activity
    public void navigateBack() {
        Intent intent = NavUtils.getParentActivityIntent(PatientMain.this);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        NavUtils.navigateUpTo(PatientMain.this, intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String pName = readNameFromPref();
        TextView p = findViewById(R.id.patientName);
        p.setText(pName);

        //Read time
        SharedPreferences timePref = this.getSharedPreferences(getString(R.string.PATIENT_TIME_KEY), Context.MODE_PRIVATE);
        int patSetStatus = timePref.getInt(getString(R.string.PATIENT_TIME_KEY), -1);
        if (patSetStatus != 1)
            timeView.setText("Not updated yet");
        else
            timeUpdated = true;
    }

    private String readNameFromPref() {
        SharedPreferences sp = this.getSharedPreferences(getString(R.string.PATIENT_NAME_KEY), Context.MODE_PRIVATE);
        return sp.getString(getString(R.string.PATIENT_NAME_KEY), DEFAULT_NAME);
    }
}
