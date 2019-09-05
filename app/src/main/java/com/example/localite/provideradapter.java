package com.example.localite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class provideradapter extends BaseAdapter {

    ArrayList<Provider> p;
    Context c;

    public provideradapter(Context c){
        this.c = c;
        p = new ArrayList<Provider>();
    }

    @Override
    public int getCount() {
        return p.size();
    }

    @Override
    public Object getItem(int i) {
        return p.get(i);
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
            view = inflator.inflate(R.layout.procard , viewGroup , false);

            TextView name = (TextView)view.findViewById(R.id.name);
            TextView addr = (TextView)view.findViewById(R.id.addr);
            TextView upvote = (TextView)view.findViewById(R.id.upvote);
            TextView downvote = (TextView)view.findViewById(R.id.downvote);

            Provider p1 = p.get(i);

            name.setText(p1.getOwnerName());
            addr.setText(p1.addr);
            upvote.setText("Upvotes:"+p1.getUpvotes());
            downvote.setText("Downvotes:"+p1.getDownvotes());
        }


        return view;
    }

    public void newpro(Provider pt){

        p.add(pt);
        notifyDataSetChanged();
    }
}
