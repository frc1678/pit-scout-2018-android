package com.jadem.androidpitscout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by niraq on 3/15/2018.
 */

public class TimerActivity extends AppCompatActivity {

    private boolean isRamp;
    private Switch timerTypeSwitch;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child();

        timerTypeSwitch = (Switch) findViewById(R.id.timerSwitch);
        isRamp = timerTypeSwitch.isChecked();
        timerTypeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRamp = isChecked;
                //TODO: Change what the listview displays (and update it?)
            }
        });

        //TODO: Add Firebase refs
    }

    public void confirmTimer(View view) {
        //TODO: Complete
    }

    public void cancelTimer(View view) {
        //TODO: Complete
    }

}
