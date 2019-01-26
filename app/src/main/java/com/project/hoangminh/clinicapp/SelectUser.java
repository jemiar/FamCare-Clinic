package com.project.hoangminh.clinicapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import static android.graphics.Typeface.BOLD;

public class SelectUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);

        TextView barTitle = (TextView) findViewById(R.id.barTitle);
        SpannableString span = new SpannableString("FAMMCARE");
        span.setSpan(new RelativeSizeSpan(0.7f), 3, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new StyleSpan(BOLD), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        barTitle.setText(span);

        TextView appName = (TextView) findViewById(R.id.appName);
        appName.setText(span);

        RecyclerView teamList = findViewById(R.id.listOfClinicians);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        teamList.setLayoutManager(layoutManager);
        UserAdapter uAdapter = new UserAdapter(this);
        teamList.setAdapter(uAdapter);

        uAdapter.addItem("C. Williams");
        uAdapter.addItem("L. Browns");
        uAdapter.addItem("C. Seigel");

        uAdapter.notifyDataSetChanged();
    }
}
