package com.jadem.androidpitscout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by niraq on 3/15/2018.
 */

public class TimerActivity extends AppCompatActivity {

    private boolean isRamp, timerRunning, success;
    private int teamNumber;
    private Button toggleButton;
    private CustomChronometer timerView;
    private Switch timerTypeSwitch;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private BaseAdapter trialAdapter;
    private Map<String, List<TrialData>> trialListMap;
    private Map<String, Long> trialCountMap;
    private ValueEventListener trialEventListener;
    private long time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //TODO: Get data from intent extras
        teamNumber = 1;//TODO: Temporary for testing, remove when done

        timerRunning = false;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Teams").child("" + teamNumber); //TODO: Receive team number before doing this!

        timerView = (CustomChronometer) findViewById(R.id.timerView);
        timerView.setText("00:00.00");
        timerView.setOnChronometerTickListener(new CustomChronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(CustomChronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h = (int)(time /3600000);
                int m = (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000 - m*60000)/1000 ;
                int ms = (int)(time - h*3600000 - m*60000 - s*1000)/10;
                String mm = m < 10 ? "0"+m: m+"";
                String ss = s < 10 ? "0"+s: s+"";
                String msms = ms < 10 ? "0"+ms: ms+"";
                cArg.setText(mm+":"+ss+"."+msms);
            }
        });

        toggleButton = (Button) findViewById(R.id.toggleTimerButton);
        timerTypeSwitch = (Switch) findViewById(R.id.timerSwitch);
        isRamp = timerTypeSwitch.isChecked();
        timerTypeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRamp = isChecked;
                //TODO: Change what the listview displays (and update it?)
            }
        });

        trialEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //TODO: Complete this (needs to update data and ListView)

                //TODO: Should these be final?
                String dTime = "pitDriveTime";
                String rTime = "pitRampTime";
                String dOut = "pitDriveTimeOutcome";
                String rOut = "pitRampTimeOutcome";
                if(dataSnapshot.hasChild(dTime) && dataSnapshot.hasChild(rTime) && dataSnapshot.hasChild(dOut) && dataSnapshot.hasChild(rOut)) {
                    if(dataSnapshot.child(dTime).hasChildren() && dataSnapshot.child(rTime).hasChildren() && dataSnapshot.child(dOut).hasChildren() && dataSnapshot.child(rOut).hasChildren()) {

                        List<TrialData> rampList = new ArrayList<TrialData>();

                        //Fills ramp list.
                        for(int trialNum = 0; trialNum < dataSnapshot.child(rTime).getChildrenCount(); trialNum++) {

                            float time = 0;
                            boolean outcome = false;
                            if(dataSnapshot.child(rTime).hasChild("" + trialNum) && dataSnapshot.child(rOut).hasChild("" + trialNum)) {
                                try {
                                    time = (Float) dataSnapshot.child(rTime).child("" + trialNum).getValue();
                                    outcome = (Boolean) dataSnapshot.child(rOut).child("" + trialNum).getValue();
                                } catch (NullPointerException npe) {
                                    Log.e("(NullPointerException", "Incorrect data type for team: " + teamNumber + ", trial: " + trialNum + ", type: ramp");
                                }
                            }

                            //If time == 0, the data for that trial is invalid.
                            TrialData data = new TrialData(time, outcome);
                            rampList.add(data);

                        }

                        List<TrialData> driveList = new ArrayList<TrialData>();

                        //Fills drive list.
                        for(int trialNum = 0; trialNum < dataSnapshot.child(dTime).getChildrenCount(); trialNum++) {

                            float time = 0;
                            boolean outcome = false;
                            if(dataSnapshot.child(dTime).hasChild("" + trialNum) && dataSnapshot.child(dOut).hasChild("" + trialNum)) {
                                try {
                                    time = (Float) dataSnapshot.child(dTime).child("" + trialNum).getValue();
                                    outcome = (Boolean) dataSnapshot.child(dOut).child("" + trialNum).getValue();
                                } catch (NullPointerException npe) {
                                    Log.e("(NullPointerException", "Incorrect data type for team: " + teamNumber + ", trial: " + trialNum + ", type: drive");
                                }
                            }

                            //If time == 0, the data for that trial is invalid.
                            TrialData data = new TrialData(time, outcome);
                            driveList.add(data);

                        }

                        trialListMap = new HashMap<>();
                        trialListMap.put("Ramp", rampList);
                        trialListMap.put("Drive", driveList);

                        trialCountMap = new HashMap<>();
                        trialCountMap.put("Ramp", dataSnapshot.child(rTime).getChildrenCount());
                        trialCountMap.put("Drive", dataSnapshot.child(dTime).getChildrenCount());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", "trialEventListener Cancelled");
                Toast connectionErrorToast = Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT);
                connectionErrorToast.setGravity(Gravity.CENTER, 0, 0);
                connectionErrorToast.show();
            }
        };
        myRef.addValueEventListener(trialEventListener);
    }

    public void toggleTimer(View view) {
        if(timerRunning) {
            //Turns timer off.
            time =  SystemClock.elapsedRealtime() - timerView.getBase(); //Stores time in milliseconds.
            timerView.stop();
            timerRunning = false;
            toggleButton.setText("Start");
        } else {
            //Turns timer on.
            time = 0;
            timerView.setBase(SystemClock.elapsedRealtime());
            timerView.start();
            timerRunning = true;
            toggleButton.setText("Stop");
        }
    }

    public void confirmTimer(View view) {
        if(!timerRunning && time != 0) {
            isRamp = timerTypeSwitch.isChecked();

            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RelativeLayout confirmDialog = (RelativeLayout) layoutInflater.inflate(R.layout.confirm_dialog, null);
            final TextView questionView = (TextView)confirmDialog.findViewById(R.id.questionView);
            final EditText distanceEditText = (EditText)confirmDialog.findViewById(R.id.distanceView);
            final EditText lengthEditText = (EditText)confirmDialog.findViewById(R.id.lengthView);

            String question = "What was the " + (isRamp ? "ramp " : "drive ") + "distance travelled?";
            questionView.setText(question);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(confirmDialog)
                    .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            String distanceString = distanceEditText.getText().toString();
                            String lengthString = lengthEditText.getText().toString();

                            float distance = 0, length = 0;
                            try {
                                distance = Float.parseFloat(distanceString);
                                length = Float.parseFloat(lengthString);

                            } catch (NumberFormatException e) {
                                Toast decimalToast = Toast.makeText(getApplicationContext(), "Invalid numbers (check for extra decimals)", Toast.LENGTH_SHORT);
                                decimalToast.setGravity(Gravity.CENTER, 0, 0);
                                decimalToast.show();
                                return;
                                //TODO: Re-open dialog with data
                            }

                            float deciTime = time;
                            deciTime = deciTime / 1000; //Stores time in seconds.

                            double ratio = 7.4; //This is the treadmill ratio.
                            boolean outcome = distance > (ratio - length);

                            //TODO: Write to firebase as an array (possibly use time in addition for ordering? - would need to get checked by Sam)
                            //TODO: Make sure that arrayPosition is updated to ramp or drive before writing (doesn't currently)
                            String typeString = isRamp ? "Ramp" : "Drive";
                            myRef.child("pit" + typeString + "Time").child("" + trialCountMap.get(typeString)).setValue(deciTime);
                            myRef.child("pit" + typeString + "TimeOutcome").child("" + trialCountMap.get(typeString)).setValue(outcome);

                            time = 0;
                            timerView.setText("00:00.00");

                            Toast successToast = Toast.makeText(getApplicationContext(), "Sent!", Toast.LENGTH_SHORT);
                            successToast.setGravity(Gravity.CENTER, 0, 0);
                            successToast.show();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
        }
    }

    public void cancelTimer(View view) {
        timerView.stop();
        time = 0;
        timerView.setText("00:00.00");
        timerRunning = false;
    }

}
