package com.jadem.androidpitscout;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> ListOfTeams;
    ListAdapter adapter2;
    EditText searchBar;
    Activity activity;
    public static FirebaseDatabase dataBase;
    public static DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataBase = FirebaseDatabase.getInstance();
        ref = dataBase.getReference();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView = (ListView) findViewById(R.id.teamsList);

        String number = "1678";
        String name = "CC";

        ListOfTeams = new ArrayList<>();
        addMessageToList(number, name, ListOfTeams);
        //listView.setAdapter(adapter);
        updateListView();
    }

    public void getTeams(View view) {
        searchBar = (EditText) findViewById(R.id.searchEditText);
        searchBar.setFocusable(false);
        updateListView();
        searchBar.setFocusableInTouchMode(true);

    }

    public void addMessageToList(String teamNumber, String teamName, ArrayList<String> ArrayListOfTeams){
        String input = teamNumber + " " + teamName;
        System.out.println(input);
        ArrayListOfTeams.add(input);
        adapter2 = new TeamListAdapter(getApplicationContext(), ArrayListOfTeams, MainActivity.this);

        listView.setAdapter(adapter2);


    }

    private void updateListView() {
        final EditText searchBar = (EditText) findViewById(R.id.searchEditText);

    }

}



