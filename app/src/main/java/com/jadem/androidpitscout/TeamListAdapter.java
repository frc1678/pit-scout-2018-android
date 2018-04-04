package com.jadem.androidpitscout;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jadem on 4/1/2018.
 */

//TODO: Probably remove this
public class TeamListAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener {

    private List<DataModel> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView teamNumber;
    }

    public TeamListAdapter(List<DataModel> data, Context context){
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        DataModel dataModel = (DataModel) object;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DataModel dataModel = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.teamNumber = (TextView) convertView.findViewById(R.id.teamNumber);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        viewHolder.teamNumber.setText(dataModel.getTeamNumber());

        return convertView;
    }
}
