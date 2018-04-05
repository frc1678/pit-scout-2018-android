package com.jadem.androidpitscout;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jadem on 3/18/2018.
 */

public class ViewTeam extends AppCompatActivity {

    int teamNumber;
    EditText SEALsNotesEditText;
    Button timerButton;
    String teamName;
    TextView teamTextView;
    TextView SEALsNotesTextView;
    Context context;
    Button sendNotesButton;
    private String key;

    FirebaseDatabase database;
    DatabaseReference dataBaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        key = null;

        context = this;
        getExtras();

        database = FirebaseDatabase.getInstance();
        dataBaseReference = database.getReference().child("Teams").child("" + teamNumber).child("pitSEALsNotes");

        SEALsNotesEditText = (EditText) findViewById(R.id.SEALsNotesEditText);
        SEALsNotesTextView = (TextView) findViewById(R.id.SEALsNotesTextView);
        teamTextView = (TextView) findViewById(R.id.teamTextView);
        sendNotesButton = (Button) findViewById(R.id.SendButton);
        timerButton = (Button) findViewById(R.id.timer);

        String teamNameString = "Team " + teamNumber + " - " + teamName;
        teamTextView.setText(teamNameString);

        SEALsNotesEditText.setFocusable(true);

    }

    public void sendNotes(View view) {

        String notes = SEALsNotesEditText.getText().toString();

        if(!notes.equals("")) {

            dataBaseReference.setValue(notes);

        }

    }

    public void openTimer(View view) {
        Intent intent = new Intent(context, TimerActivity.class);
        intent.putExtra("teamNumber", teamNumber);
        startActivity(intent);
    }

    private void getExtras() {
        Intent previous = getIntent();
        teamNumber = previous.getIntExtra("teamNumber", 0);
        teamName = previous.getStringExtra("teamName");
    }

}
