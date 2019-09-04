package com.example.localite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CardAdapter extends BaseAdapter {

    ArrayList<Card> cardlist;
    Context c;

    public CardAdapter(Context c){
        this.c = c;
        cardlist = new ArrayList<Card>();
    }

    @Override
    public int getCount() {
        return cardlist.size();
    }

    @Override
    public Object getItem(int i) {
        return cardlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null)
        {
            LayoutInflater inflator = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflator.inflate(R.layout.card , viewGroup , false);

            TextView txtType = (TextView)view.findViewById(R.id.ctext);

            Card c = cardlist.get(i);

            txtType.setText(c.getType());
        }

        return view;
    }
}
