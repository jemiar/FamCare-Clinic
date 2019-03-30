package com.project.hoangminh.clinicapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.content.res.AppCompatResources;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.graphics.Typeface.BOLD;

public class Settings extends AppCompatActivity {

    private Button setInfoBtn, sendInviBtn, addClinicianBtn;
    static final int CONFIRM = 11;
//    private Boolean pName = false;
//    private Boolean pCode = false;
    private Boolean cName = false;
    private Boolean cCode = false;
    private EditText patientName, patientCode, email, clinicianName, clinicianCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView barTitle = findViewById(R.id.barTitle);
        SpannableString span = new SpannableString("FAMMCARE");
        span.setSpan(new RelativeSizeSpan(0.7f), 3, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new StyleSpan(BOLD), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        barTitle.setText(span);

        setInfoBtn = findViewById(R.id.setInfoButton);
        setInfoBtn.setEnabled(false);
        setInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Settings.this, ConfirmSetting.class);
                i.putExtra("CODE", 1);
                String patName = patientName.getText().toString();
                i.putExtra("NAME", patName);
                i.putExtra("QUESTION", "Do you want to set " + patName + " as patient?");
                startActivityForResult(i, CONFIRM);
            }
        });

        sendInviBtn = findViewById(R.id.sendInviButton);
        sendInviBtn.setEnabled(false);
        sendInviBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get email address from email input field
                String[] email_address = {email.getText().toString()};

                //set email input field to empty
                email.setText("");

                //Get patient code from Shared Preferences
                SharedPreferences sp = Settings.this.getSharedPreferences(getString(R.string.PATIENT_CODE), Context.MODE_PRIVATE);
                String p_code = sp.getString(getString(R.string.PATIENT_CODE), "default");

                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setData(Uri.parse("mailto:"));
                i.putExtra(Intent.EXTRA_EMAIL, email_address);

                String subject = "Invitation to use famCARE app";
                i.putExtra(Intent.EXTRA_SUBJECT, subject);

                String email_content = "Please use the famCARE app with this code " + p_code;
                i.putExtra(Intent.EXTRA_TEXT, email_content);

                if (i.resolveActivity(getPackageManager()) != null)
                    startActivity(i);

            }
        });

        addClinicianBtn = findViewById(R.id.addClinicianButton);
        addClinicianBtn.setEnabled(false);
        addClinicianBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Settings.this, ConfirmSetting.class);
                i.putExtra("CODE", 2);
                String cliName = clinicianName.getText().toString();
                i.putExtra("NAME", cliName);
                i.putExtra("QUESTION", "Do you want to add " + cliName + " to the care team?");
                startActivityForResult(i, CONFIRM);
            }
        });

        patientName = findViewById(R.id.pname_input);
        patientName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() > 0)
                    enableButton(setInfoBtn);
                else
                    disableButton(setInfoBtn);
//                if(s.toString().trim().length() > 0) {
//                    pName = true;
//                    if(pCode)
//                        enableButton(setInfoBtn);
//                    else
//                        disableButton(setInfoBtn);
//                } else {
//                    pName = false;
//                    disableButton(setInfoBtn);
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

//        patientCode = findViewById(R.id.pcode_input);
//        patientCode.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(s.toString().trim().length() > 0) {
//                    pCode = true;
//                    if(pName)
//                        enableButton(setInfoBtn);
//                    else
//                        disableButton(setInfoBtn);
//                } else {
//                    pCode = false;
//                    disableButton(setInfoBtn);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });

        email = findViewById(R.id.email_input);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() > 0)
                    enableButton(sendInviBtn);
                else
                    disableButton(sendInviBtn);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        clinicianName = findViewById(R.id.clinician_input);
        clinicianName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() > 0) {
                    cName = true;
                    if(cCode)
                        enableButton(addClinicianBtn);
                    else
                        disableButton(addClinicianBtn);
                } else {
                    cName = false;
                    disableButton(addClinicianBtn);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        clinicianCode = findViewById(R.id.pw_input);
        clinicianCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() > 0) {
                    cCode = true;
                    if(cName)
                        enableButton(addClinicianBtn);
                    else
                        disableButton(addClinicianBtn);
                } else {
                    cCode = false;
                    disableButton(addClinicianBtn);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CONFIRM) {
            if (resultCode == RESULT_OK) {
                int i = data.getIntExtra("CODE", 0);
                switch (i) {
                    case 1:
                        patientName.setText("");
                        break;
                    case 2:
                        clinicianName.setText("");
                        clinicianCode.setText("");
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void enableButton(Button b) {
        b.setEnabled(true);
        ViewCompat.setBackgroundTintList(b, AppCompatResources.getColorStateList(Settings.this, R.color.white));
        b.setTextColor(getResources().getColor(R.color.black));
    }

    public void disableButton(Button b) {
        b.setEnabled(false);
        ViewCompat.setBackgroundTintList(b, AppCompatResources.getColorStateList(Settings.this, R.color.gray));
        b.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onBackPressed() {
        Intent intent = NavUtils.getParentActivityIntent(Settings.this);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        NavUtils.navigateUpTo(Settings.this, intent);
    }
}
