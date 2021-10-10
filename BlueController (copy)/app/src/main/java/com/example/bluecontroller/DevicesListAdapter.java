package com.example.bluecontroller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class DevicesListAdapter extends ArrayAdapter<Devices> {
    private static final String TAG = "DevicesListAdapter";
    private Context mContext;
    int mResourse;

    public DevicesListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Devices> objects) {
        super(context, resource, objects);
        mContext = context;
        mResourse = resource;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String cName = getItem(position).getcName();
        String address = getItem(position).getAddress();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResourse, parent, false);
        TextView t1 = convertView.findViewById(R.id.text1);
        TextView t2 = convertView.findViewById(R.id.text2);

        t1.setText(cName);
        t2.setText(address);

        return convertView;
    }
}
