package com.example.mymusic.radio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.mymusic.R;

import java.util.ArrayList;


public class RadioListAdapter extends BaseAdapter {
    private LayoutInflater lInflater;
    private ArrayList<RadioStation> objects;

    public RadioListAdapter(Context context, ArrayList<RadioStation> products) {
         objects = products;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return objects.size();
    }

      @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) view = lInflater.inflate(R.layout.radiolist, parent, false);
        RadioStation p = getProduct(position);
        ((TextView) view.findViewById(R.id.radioTxt)).setText(p.txt);
        ((ImageView) view.findViewById(R.id.radioIco)).setImageResource(p.radioIco);
        ((ImageView) view.findViewById(R.id.radioPlay)).setImageResource(p.radioPlay);
    return view;
    }
    private RadioStation getProduct(int position) {
        return ((RadioStation) getItem(position));
    }
}
