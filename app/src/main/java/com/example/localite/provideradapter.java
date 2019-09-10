package com.example.localite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class provideradapter extends BaseAdapter {

    TextView name;
    TextView addr;
    TextView upvote;
    TextView downvote;

    ArrayList<Provider> p;
    Context c;

    DatabaseReference databaseReference;

    Provider p1;

    public provideradapter(Context c){
        this.c = c;
        p = new ArrayList<Provider>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Provider");
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

            name = (TextView)view.findViewById(R.id.name);
            addr = (TextView)view.findViewById(R.id.addr);
            upvote = (TextView)view.findViewById(R.id.upvote);
            downvote = (TextView)view.findViewById(R.id.downvote);

            p1 = p.get(i);

            name.setText(p1.getOwnerName());
            addr.setText(p1.addr);
            upvote.setText(String.valueOf(p1.getUpvotes()));
            downvote.setText(String.valueOf(p1.getDownvotes()));

            upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    p1.upvotes++;
                    upvote.setText(String.valueOf(p1.getUpvotes()));
                    databaseReference.child(p1.bussType).child(p1.getPhone()).setValue(p1);
                }
            });

            downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    p1.downvotes++;
                    downvote.setText(String.valueOf(p1.getDownvotes()));
                    databaseReference.child(p1.bussType).child(p1.getPhone()).setValue(p1);
                }
            });
        }


        return view;
    }

    public void newpro(Provider pt){

        p.add(pt);
        notifyDataSetChanged();
    }
}
