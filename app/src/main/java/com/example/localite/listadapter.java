package com.example.localite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class listadapter extends BaseAdapter {

    ArrayList<String> key;
    Context c;

    public listadapter(ArrayList<String> name ,Context c) {
        this.key = name;
        this.c = c;
    }

    @Override
    public int getCount() {
        return key.size();
    }

    @Override
    public Object getItem(int i) {
        return key.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){

            LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.single_chat_history , viewGroup , false);
        }

        String k = key.get(i);
        int index = k.indexOf(",");

        String name = k.substring(index+1);

        TextView circle = (TextView)view.findViewById(R.id.circle);
        TextView n = (TextView)view.findViewById(R.id.name);

        String firstletter = name.substring(0 , 1);

        circle.setText(firstletter);
        n.setText(name);

        return view;
    }
}
