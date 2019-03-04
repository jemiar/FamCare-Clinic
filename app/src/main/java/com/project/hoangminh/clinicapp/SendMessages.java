package com.project.hoangminh.clinicapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.graphics.Typeface.BOLD;

public class SendMessages extends AppCompatActivity {

    private Button sendBtn;
    private FirebaseDatabase famCareDB;
    private DatabaseReference dbRef;
    private ChildEventListener newMsgListener;
    private EditText msgEdit;
    private RecyclerView msgArea;
    private LinearLayoutManager msgLayoutManager;
    private MessageAdapter msgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        TextView title = (TextView) findViewById(R.id.title_patientMain);
        SpannableString span = new SpannableString("FAMMCARE");
        span.setSpan(new RelativeSizeSpan(0.7f), 3, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new StyleSpan(BOLD), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        title.setText(span);

        ImageView backBtn = (ImageView) findViewById(R.id.backBtn_patientMain);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SendMessages.this, PatientMain.class);
                startActivity(intent);
            }
        });

        //Get the instance of the database
        famCareDB = FirebaseDatabase.getInstance();
        //Only interest in the "messages" node -> Get the reference to it
        dbRef = famCareDB.getReference().child("messages");

        sendBtn = findViewById(R.id.sendButton);
        sendBtn.setEnabled(false);

        msgEdit = (EditText) findViewById(R.id.msgEditText);

        //Add event listener to the message input area
        msgEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            //When there is text changed, if there is text, set the [SEND] button to enabled state
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length() > 0) {
                    sendBtn.setEnabled(true);
                    ViewCompat.setBackgroundTintList(sendBtn, AppCompatResources.getColorStateList(SendMessages.this, R.color.btnColor));
                } else {
                    sendBtn.setEnabled(false);
                    ViewCompat.setBackgroundTintList(sendBtn, AppCompatResources.getColorStateList(SendMessages.this, R.color.gray));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        msgArea = (RecyclerView) findViewById(R.id.messageArea);
        //For optimization, when you know your recyclerview size doesn't change
        //Allow OS to use the same layout without creating new one each time
        msgArea.setHasFixedSize(true);
        msgLayoutManager = new LinearLayoutManager(this);
        //New item is added from the the bottom of the recycler view
        msgLayoutManager.setStackFromEnd(true);
        msgArea.setLayoutManager(msgLayoutManager);
        msgAdapter = new MessageAdapter(this);
        msgArea.setAdapter(msgAdapter);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create new message
                FamCareMessage msg = new FamCareMessage("Clinic", msgEdit.getText().toString());
                //Push to database
                dbRef.push().setValue(msg);
                //Reset the text input area
                msgEdit.setText("");
            }
        });

        //Create new event listener for database reference
        newMsgListener = new ChildEventListener() {
            //Invoke when there is new data added or when the 1st time the listener is attached to the database
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Get new messages from database's data snapshot
                FamCareMessage msg = dataSnapshot.getValue(FamCareMessage.class);
                //Add new messages to the adapter's message linkedlist
                msgAdapter.addMessage(msg);
                //notify the adapter that there are new messages
                msgAdapter.notifyDataSetChanged();
                //scroll to the latest message added to the recyclerview
                msgArea.smoothScrollToPosition(msgAdapter.getItemCount() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        //Add event listener to database reference
        dbRef.addChildEventListener(newMsgListener);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SendMessages.this, PatientMain.class);
        startActivity(intent);
    }
}
