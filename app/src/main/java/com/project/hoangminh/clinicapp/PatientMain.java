package com.project.hoangminh.clinicapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static android.graphics.Typeface.BOLD;

public class PatientMain extends AppCompatActivity {

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
                Intent intent = new Intent(PatientMain.this, SelectUser.class);
                startActivity(intent);
            }
        });

        Button vitalsBtn = (Button) findViewById(R.id.vitalButton);
        vitalsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientMain.this, UpdateVitals.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PatientMain.this, SelectUser.class);
        startActivity(intent);
    }
}
