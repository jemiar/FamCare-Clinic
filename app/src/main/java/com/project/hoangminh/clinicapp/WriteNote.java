package com.project.hoangminh.clinicapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.graphics.Typeface.BOLD;

public class WriteNote extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private Button sendBtn;
    private FirebaseDatabase famCareDB;
    private DatabaseReference dbRef;
    private ChildEventListener newMsgListener;
    private EditText noteEdit;
    private RecyclerView noteArea;
    private LinearLayoutManager noteLayoutManager;
    private NoteAdapter nAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        TextView title = (TextView) findViewById(R.id.title_patientMain);
        SpannableString span = new SpannableString("FAMMCARE");
        span.setSpan(new RelativeSizeSpan(0.7f), 3, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new StyleSpan(BOLD), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        title.setText(span);

        ImageView backBtn = (ImageView) findViewById(R.id.backBtn_patientMain);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WriteNote.this, PatientMain.class);
                startActivity(intent);
            }
        });

        //Set hamburger button
        ImageView options = findViewById(R.id.optionBtn_patientMain);
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(WriteNote.this, v);
                popup.setOnMenuItemClickListener(WriteNote.this);
                popup.inflate(R.menu.hamburger_menu);
                popup.show();
            }
        });

        //Read patient code from Shared Preferences
        SharedPreferences sp = this.getSharedPreferences(getString(R.string.PATIENT_CODE), Context.MODE_PRIVATE);
        String p_code = sp.getString(getString(R.string.PATIENT_CODE), "default");

        //Get the instance of the database
        famCareDB = FirebaseDatabase.getInstance();
        //Only interest in the "messages" node -> Get the reference to it
        dbRef = famCareDB.getReference().child(p_code).child("notes");

        sendBtn = findViewById(R.id.noteSendButton);
        sendBtn.setEnabled(false);

        noteEdit = (EditText) findViewById(R.id.noteEditText);

        //Add event listener to the message input area
        noteEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            //When there is text changed, if there is text, set the [SEND] button to enabled state
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length() > 0) {
                    sendBtn.setEnabled(true);
                    ViewCompat.setBackgroundTintList(sendBtn, AppCompatResources.getColorStateList(WriteNote.this, R.color.btnColor));
                } else {
                    sendBtn.setEnabled(false);
                    ViewCompat.setBackgroundTintList(sendBtn, AppCompatResources.getColorStateList(WriteNote.this, R.color.gray));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        noteArea = (RecyclerView) findViewById(R.id.noteArea);
        //For optimization, when you know your recyclerview size doesn't change
        //Allow OS to use the same layout without creating new one each time
        noteArea.setHasFixedSize(true);
        noteLayoutManager = new LinearLayoutManager(this);
        //New item is added from the the bottom of the recycler view
//        noteLayoutManager.setStackFromEnd(true);
        noteArea.setLayoutManager(noteLayoutManager);
        nAdapter = new NoteAdapter();
        noteArea.setAdapter(nAdapter);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create new message
                NoteItem note = new NoteItem(noteEdit.getText().toString());
                //Push to database
                dbRef.push().setValue(note);
                //Reset the text input area
                noteEdit.setText("");
            }
        });

        //Create new event listener for database reference
        newMsgListener = new ChildEventListener() {
            //Invoke when there is new data added or when the 1st time the listener is attached to the database
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Get new messages from database's data snapshot
                NoteItem note = dataSnapshot.getValue(NoteItem.class);
                //Add new messages to the adapter's message linkedlist
                nAdapter.addItem(note);
                //notify the adapter that there are new messages
                nAdapter.notifyDataSetChanged();
                //scroll to the latest message added to the recyclerview
                noteArea.smoothScrollToPosition(nAdapter.getItemCount() - 1);
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

        //Add event listener to database reference
        dbRef.addChildEventListener(newMsgListener);
    }

    @Override
    public void onBackPressed() {
        navigateBack();
    }

    //Implement menu click function
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent i = new Intent(WriteNote.this, Settings.class);
                startActivity(i);
                return true;
            case R.id.signout:
                Intent intent_out = new Intent(WriteNote.this, SelectUser.class);
                startActivity(intent_out);
                return true;
            default:
                return false;
        }
    }

    //Function to navigate back
    //Need to do this to keep the state of the parent activity
    public void navigateBack() {
        Intent intent = NavUtils.getParentActivityIntent(WriteNote.this);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        NavUtils.navigateUpTo(WriteNote.this, intent);
    }
}
