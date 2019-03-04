package com.project.hoangminh.clinicapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmRemove extends AppCompatActivity {

    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_remove);

        TextView question = findViewById(R.id.confirm_Q);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        question.setText("Do you want to remove " + name + " from care team for this patient?");
        pos = intent.getIntExtra("position", -1);

        Button cancelBtn = (Button) findViewById(R.id.remove_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button okBtn = findViewById(R.id.remove_OK);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmRemove.this, SelectUser.class);
                intent.putExtra("position", pos);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
