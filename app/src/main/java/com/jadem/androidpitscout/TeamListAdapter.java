package com.jadem.androidpitscout;

/**
 * Created by yudiw on 3/17/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jadem.androidpitscout.R;

import java.util.ArrayList;

/**
 * Created by yudiw on 3/17/2018.
 */

public class TeamListAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> myArrayList;
    Activity activity;

    public TeamListAdapter(Context context, ArrayList <String> array, Activity activity) {
        super();
        this.context = context;
        this.myArrayList = array;
        this.activity = activity;
    }
    @Override
    public int getCount(){
        return myArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String message = myArrayList.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myLayout = inflater.inflate(R.layout.cell_layout, null);
        TextView messageTextView = (TextView) myLayout.findViewById(R.id.Teams);
        messageTextView.setText(message);
        convertView = myLayout;

        return convertView;
    }
}