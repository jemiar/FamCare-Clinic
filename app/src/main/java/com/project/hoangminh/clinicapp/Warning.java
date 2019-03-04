package com.project.hoangminh.clinicapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class Warning extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning);
        ListView lv = findViewById(R.id.listView);
        ArrayList<String> vals = getIntent().getStringArrayListExtra("values");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.txtview, vals);
        lv.setAdapter(adapter);

        Button okBtn = findViewById(R.id.ok_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
