package com.jadem.androidpitscout;

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

/**
 * Created by jadem on 3/18/2018.
 */

public class ViewTeam extends AppCompatActivity {

    EditText SEALsNotesEditText;
    Button TimerButton;
    EditText TeamNumber;
    TextView SEALsNotesTextView;
    FirebaseDatabase dataBase;
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SEALsNotesEditText = (EditText) findViewById(R.id.SEALsNotesEditText);
        SEALsNotesTextView = (TextView) findViewById(R.id.SEALsNotesTextView);
        TimerButton = (Button) findViewById(R.id.timer);
        TeamNumber = (EditText) findViewById(R.id.teamNumber);

        dataBase = FirebaseDatabase.getInstance();
        ref = dataBase.getReference().child("Teams").child("" + TeamNumber);

        SEALsNotesEditText.setFocusable(true);
        TeamNumber.setFocusable(true);

        TimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewTeam.this, TimerActivity.class));
            }
        });
    }


}

