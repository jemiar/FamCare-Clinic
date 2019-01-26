package com.project.hoangminh.clinicapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static android.graphics.Typeface.BOLD;

public class UpdateVitals extends AppCompatActivity {

    private RecyclerView vitalArea;
    private LinearLayoutManager layoutMan;
    private VitalAdapter vAdapter;

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

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UpdateVitals.this, PatientMain.class);
        startActivity(intent);
    }
}
