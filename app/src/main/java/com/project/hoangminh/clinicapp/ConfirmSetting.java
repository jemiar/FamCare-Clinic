package com.project.hoangminh.clinicapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ConfirmSetting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_setting);
        final Intent intent = getIntent();
        String question = intent.getStringExtra("QUESTION");
        TextView q = findViewById(R.id.confirm_text);
        q.setText(question);
        final int code = intent.getIntExtra("CODE", 0);
        Button cancelButton = findViewById(R.id.setting_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button confirmButton = findViewById(R.id.setting_OK);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (code) {
                    case 1:
                        String n = intent.getStringExtra("NAME");
                        setPatient(n);
                        break;
                    case 2:
                        String clinician = intent.getStringExtra("NAME");
                        addClinician(clinician);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void setPatient(String s) {

        //Write name to SharedPreferences
        writeToPref(R.string.PATIENT_NAME_KEY, 1, s, 10);
        //Write time to SharedPreferences
        writeToPref(R.string.PATIENT_TIME_KEY, 2, "", 0);
        //Set patient's goal to default
        writeToPref(R.string.PATIENT_GOAL, 1, getString(R.string.goal_initial), 10);

        //Thread to query patients' code
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                final CollectionReference CODESREF = db.collection("codes");
                final String initialCode = generateRandomWord();
                Query query = CODESREF.whereEqualTo("key", initialCode);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() <= 0) {
                                addDocument(CODESREF, initialCode);
                            } else {
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    String c = (String)doc.getData().get("key");
                                    String newCode = c.substring(0,5) + (Integer.parseInt(c.substring(5) + 1));
                                    addDocument(CODESREF, newCode);
                                }
                            }
                        } else
                            Log.i("KOL", "Error");
                    }
                });
            }
        };
        new Thread(runnable).start();

        returnIntent(1);
    }

    private void addClinician(String c) {
        SharedPreferences sp = this.getSharedPreferences(getString(R.string.CLINICIAN_LIST), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        //Read from Shared Preferences first
        String json = sp.getString(getString(R.string.CLINICIAN_LIST), "default");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> clinician_list = gson.fromJson(json, type);

        //Add new clinician to list
        clinician_list.add(c);
        Log.i("LIST_AM", "num: " + clinician_list.size());
        String new_json = gson.toJson(clinician_list);
        editor.putString(getString(R.string.CLINICIAN_LIST),  new_json);
        editor.apply();

        returnIntent(2);
    }

    //Function to return intent to the calling activity
    private void returnIntent(int i) {
        Intent intent = new Intent(ConfirmSetting.this, Settings.class);
        intent.putExtra("CODE", i);
        setResult(RESULT_OK, intent);
        finish();
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

    //Function to generate random patient code from the alphabet table
    private String generateRandomWord() {
        Random random = new Random();
        char[] word = new char[6];
        for (int i = 0; i < 5; i++) {
            word[i] = (char)('a' + random.nextInt(26));
        }
        word[5] = '0';
        return new String(word);
    }

    //Add patient code to Firestore database: codes collection
    //Write patient code to SharedPreferences
    //Write initial vitals values to Realtime DB
    private void addDocument(CollectionReference colRef, String code) {
        Map<String, String> data = new HashMap<>();
        data.put("key", code);
        writeToPref(R.string.PATIENT_CODE, 1, code, 10);

        //Write initial vital data to DB
        writeInitialVitalsToDB(code);

        colRef.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.i("KOL", "Doc added");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("KOL", "Doc added fail");
            }
        });
    }

    //Function to write intial vital data to Realtime DB
    private void writeInitialVitalsToDB(String code) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference().child(code).child("vitals");

        ArrayList<String> labels = new ArrayList<>(Arrays.asList("Heart Rate", "Respiratory Rate",
                "Blood Pressure", "Body Temperature", "Blood Oxygen", "Pain", "Sedation", "Awake",
                "Night Sleep", "Medical Compliance", "Intravenous", "Feeding Tube", "Appetite"));

        ArrayList<ReviewItem> dataset = new ArrayList<>();

        for(int i = 0; i < labels.size(); i++) {
            dataset.add(new ReviewItem(labels.get(i), "N / A"));
        }

        ref.push().setValue(dataset);
    }
}
