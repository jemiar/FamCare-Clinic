package com.project.hoangminh.clinicapp;

import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.content.res.AppCompatResources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SetGoal extends AppCompatActivity {

    private EditText goalEdit;
    private Button goalOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goal);



        Button goalCancel = findViewById(R.id.goalCancel);
        goalCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        goalOk = findViewById(R.id.goalOk);
        goalOk.setEnabled(false);
        goalOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetGoal.this, UpdateVitals.class);
                intent.putExtra("goal", goalEdit.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        goalEdit = findViewById(R.id.goalText);
        goalEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            //When there is text changed, if there is text, set the [SEND] button to enabled state
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length() > 0) {
                    goalOk.setEnabled(true);
                    ViewCompat.setBackgroundTintList(goalOk, AppCompatResources.getColorStateList(SetGoal.this, R.color.btnColor));
                } else {
                    goalOk.setEnabled(false);
                    ViewCompat.setBackgroundTintList(goalOk, AppCompatResources.getColorStateList(SetGoal.this, R.color.gray));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}
