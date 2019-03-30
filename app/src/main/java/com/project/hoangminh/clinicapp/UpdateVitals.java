package com.project.hoangminh.clinicapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static android.graphics.Typeface.BOLD;

public class UpdateVitals extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    static final int SET_GOAL = 1;

    private RecyclerView vitalArea;
    private LinearLayoutManager layoutMan;
    private VitalAdapter vAdapter;
    private FirebaseDatabase famCareDB;
    private DatabaseReference dbRef;
    private ChildEventListener vitalListener;
    private TextView hrVal, btVal, bpVal, boVal, rrVal;
    private TextView goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_vitals);

        TextView title = (TextView) findViewById(R.id.title_patientMain);
        SpannableString span = new SpannableString("FAMMCARE");
        span.setSpan(new RelativeSizeSpan(0.7f), 3, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new StyleSpan(BOLD), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        title.setText(span);

        ImageView backBtn = (ImageView) findViewById(R.id.backBtn_patientMain);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateVitals.this, PatientMain.class);
                startActivity(intent);
            }
        });

        //Set hamburger button
        ImageView options = findViewById(R.id.optionBtn_patientMain);
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(UpdateVitals.this, v);
                popup.setOnMenuItemClickListener(UpdateVitals.this);
                popup.inflate(R.menu.hamburger_menu);
                popup.show();
            }
        });


        //Get TextView for item summary
        hrVal = findViewById(R.id.hr_val);
        btVal = findViewById(R.id.bt_val);
        bpVal = findViewById(R.id.bp_val);
        boVal = findViewById(R.id.bo_val);
        rrVal = findViewById(R.id.rr_val);

        //Get TextView for patient goal
        goal = findViewById(R.id.goal_txt);
        goal.setText(readGoalFromPref());

        Button submit_btn = findViewById(R.id.submitBtn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> responses = (ArrayList<String>)vAdapter.getResponses();
                if(responses.contains("NOVALUE")) {
                    int first = responses.indexOf("NOVALUE");
                    int last = responses.lastIndexOf("NOVALUE");
                    ArrayList<String> empties = new ArrayList<>();
                    for(int i = first; i <= last; i++) {
                        if(responses.get(i).equals("NOVALUE"))
                            empties.add(vAdapter.getDataSet().get(i).getLabel());
                    }
                    Intent intent = new Intent(UpdateVitals.this, Warning.class);
                    intent.putStringArrayListExtra("values", empties);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(UpdateVitals.this, Review.class);
                    intent.putStringArrayListExtra("values", (ArrayList<String>) vAdapter.getResponses());
                    startActivity(intent);
                }
            }
        });

        //Set patient goal
        Button editGoalBtn = findViewById(R.id.edit_goal_btn);
        editGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateVitals.this, SetGoal.class);
                startActivityForResult(intent, SET_GOAL);
            }
        });

        vitalArea = findViewById(R.id.input_area);
        vitalArea.setHasFixedSize(true);
        layoutMan = new LinearLayoutManager(this);
        vitalArea.setLayoutManager(layoutMan);
        vAdapter = new VitalAdapter(this);
        vitalArea.setAdapter(vAdapter);

        Vital_Item heartRate = new Vital_Item("Heart Rate", "Low", "Normal", "High", false);
        vAdapter.addItem(heartRate);
        Vital_Item respRate = new Vital_Item("Respiratory Rate", "Low", "Normal", "High", false);
        vAdapter.addItem(respRate);
        Vital_Item bloodPress = new Vital_Item("Blood Pressure", "Low", "Normal", "High", false);
        vAdapter.addItem(bloodPress);
        Vital_Item bodyTemp = new Vital_Item("Body Temperature", "Low", "Normal", "High", false);
        vAdapter.addItem(bodyTemp);
        Vital_Item bloodO2 = new Vital_Item("Blood Oxygen", "Low", "Normal", "High", true);
        vAdapter.addItem(bloodO2);
        Vital_Item pain = new Vital_Item("Pain", "Decreasing", "Normal", "Increasing", false);
        vAdapter.addItem(pain);
        Vital_Item sedation = new Vital_Item("Sedation", "Awake", "Light", "Deep", false);
        vAdapter.addItem(sedation);
        Vital_Item awake = new Vital_Item("Awake", "Responsive", "Partial", "Not Responsive", false);
        vAdapter.addItem(awake);
        Vital_Item sleep = new Vital_Item("Night Sleep", "Restless", "Fair", "Good", false);
        vAdapter.addItem(sleep);
        Vital_Item med = new Vital_Item("Medical Compliance", "Negative", "No Response", "Positive", false);
        vAdapter.addItem(med);
        Vital_Item iv = new Vital_Item("Intravenous", "No", "Yes", "Increase", true);
        vAdapter.addItem(iv);
        Vital_Item tube = new Vital_Item("Feeding Tube", "No", "Yes", "Increase", true);
        vAdapter.addItem(tube);
        Vital_Item appetite = new Vital_Item("Appetite", "Poor", "Good", "Excellent", false);
        vAdapter.addItem(appetite);
        vAdapter.notifyDataSetChanged();

        //Set the day
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
        String time = "" + months[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.DAY_OF_MONTH) + ", " + cal.get(Calendar.YEAR);
        TextView day = findViewById(R.id.day_month);
        day.setText(time);

        //Read patient code from Shared Preferences, in order to read vitals from realtime DB
        String p_code = readPatientCodeFromPref();

        //Update summary panel
        famCareDB = FirebaseDatabase.getInstance();
        dbRef = famCareDB.getReference().child(p_code).child("vitals");
        vitalListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("AMTRAK", "Child added");
                GenericTypeIndicator<List<ReviewItem>> t = new GenericTypeIndicator<List<ReviewItem>>(){};
                List<ReviewItem> rItems = dataSnapshot.getValue(t);
                String hr = rItems.get(0).getVal().toUpperCase();
                String bt = rItems.get(3).getVal().toUpperCase();
                String bo = rItems.get(4).getVal().toUpperCase();
                String rr = rItems.get(1).getVal().toUpperCase();
                setMarginStart(hr, hrVal, 0);
                setMarginStart(bt, btVal, 1);
                setMarginStart(bo, boVal, 2);
                setMarginStart(rr, rrVal, 3);
                hrVal.setText(hr);
                btVal.setText(bt);
                bpVal.setText(rItems.get(2).getVal().toUpperCase());
                boVal.setText(bo);
                rrVal.setText(rr);
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
        dbRef.addChildEventListener(vitalListener);

    }

    @Override
    public void onBackPressed() {
        navigateBack();
    }

    private void setMarginStart(String s, TextView t, int i) {
        int[] lows = {45, 215, 555, 725};
        int[] norms = {40, 210, 550, 720};
        int[] highs = {41, 212, 550, 723};
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) t.getLayoutParams();
        switch (s) {
            case "LOW":
                params.setMarginStart(lows[i]);
                break;
            case "NORMAL":
                params.setMarginStart(norms[i]);
                break;
            case "HIGH":
                params.setMarginStart(highs[i]);
                break;
            default:
                break;
        }
        t.setLayoutParams(params);
    }

    //Get result from set goal activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == SET_GOAL) {
            if(resultCode == RESULT_OK) {
                goal.setText(data.getStringExtra("goal"));
                //Write goal to shared preferences
                SharedPreferences sp = this.getSharedPreferences(getString(R.string.PATIENT_GOAL), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(getString(R.string.PATIENT_GOAL), data.getStringExtra("goal"));
                editor.apply();
            }
        }
    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        goal.setText(savedInstanceState.getString("GOAL"));
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        outState.putString("GOAL", goal.getText().toString());
//        Log.i("AMTRAK", "Saved");
//        super.onSaveInstanceState(outState);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.PATIENT_NAME_KEY), Context.MODE_PRIVATE);
        String defaultValue = "Patient not yet set";
        String pName = sharedPref.getString(getString(R.string.PATIENT_NAME_KEY), defaultValue);
        TextView p = findViewById(R.id.patName);
        p.setText(pName);

        //Read goal from shared preferences, then set text of the goal field
        String g = readGoalFromPref();
        goal.setText(g);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //Implement menu click function
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent i = new Intent(UpdateVitals.this, Settings.class);
                startActivity(i);
                return true;
            case R.id.signout:
                Intent intent_out = new Intent(UpdateVitals.this, SelectUser.class);
                startActivity(intent_out);
                return true;
            default:
                return false;
        }
    }

    private String readGoalFromPref() {
        SharedPreferences sp = this.getSharedPreferences(getString(R.string.PATIENT_GOAL), Context.MODE_PRIVATE);
        String g = sp.getString(getString(R.string.PATIENT_GOAL), getString(R.string.goal_initial));
        return g;
    }

    //Function to read patient code from Shared Preferences
    private String readPatientCodeFromPref() {
        SharedPreferences sp = this.getSharedPreferences(getString(R.string.PATIENT_CODE), Context.MODE_PRIVATE);
        String g = sp.getString(getString(R.string.PATIENT_CODE), "default");
        return g;
    }

    //Function to navigate back
    //Need to do this to keep the state of the parent activity
    public void navigateBack() {
        Intent intent = NavUtils.getParentActivityIntent(UpdateVitals.this);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        NavUtils.navigateUpTo(UpdateVitals.this, intent);
    }

}
