package com.project.hoangminh.clinicapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.graphics.Typeface.BOLD;

public class SelectUser extends AppCompatActivity {

    private UserAdapter uAdapter;

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
        uAdapter = new UserAdapter(this);
        teamList.setAdapter(uAdapter);

//        uAdapter.addItem("C. Williams");
//        uAdapter.addItem("L. Browns");
//        uAdapter.addItem("C. Seigel");
//
//        uAdapter.notifyDataSetChanged();

        //Read from clinician list
        SharedPreferences sp = this.getSharedPreferences(getString(R.string.CLINICIAN_LIST), Context.MODE_PRIVATE);
        String json = sp.getString(getString(R.string.CLINICIAN_LIST), "default");
        Gson gson = new Gson();
        //Check the 1st time use
        if (json.equals("default")) {
            ArrayList<String> clist = new ArrayList<>();
            clist.add("Admin");
            String new_json = gson.toJson(clist);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(getString(R.string.CLINICIAN_LIST), new_json);
            editor.apply();
            uAdapter.addItem("Admin");
            uAdapter.notifyDataSetChanged();
        } else {
            //Not the first time
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            ArrayList<String> clinician_list = gson.fromJson(json, type);

            //Add new clinician if adding a new clinician in setting
            uAdapter.setDataset(clinician_list);
            uAdapter.notifyDataSetChanged();
//            //Add new clinician if adding a new clinician in setting
//            if (uAdapter.getItemCount() < clinician_list.size()) {
//                uAdapter.addItem(clinician_list.get(clinician_list.size() - 1));
//                uAdapter.notifyDataSetChanged();
//            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == UserAdapter.REMOVE) {
            if(resultCode == RESULT_OK) {
                int pos = data.getIntExtra("position", -1);
                if(pos != -1) {
                    uAdapter.removeItem(pos);
                    uAdapter.notifyDataSetChanged();

                    //Write to Shared Preferences
                    SharedPreferences sp = this.getSharedPreferences(getString(R.string.CLINICIAN_LIST), Context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = gson.toJson(uAdapter.getDataset());
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(getString(R.string.CLINICIAN_LIST), json);
                    editor.apply();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = this.getSharedPreferences(getString(R.string.CLINICIAN_LIST), Context.MODE_PRIVATE);
        String json = sp.getString(getString(R.string.CLINICIAN_LIST), "default");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> clinician_list = gson.fromJson(json, type);
        uAdapter.setDataset(clinician_list);
        uAdapter.notifyDataSetChanged();
    }
}
