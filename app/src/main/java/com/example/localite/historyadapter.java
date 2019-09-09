package com.example.localite;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class historyadapter extends RecyclerView.Adapter<historyadapter.nameholder> {

    ArrayList<String> name;
    ArrayList<String> phonenumber;
    Context c;

    public historyadapter(Context c) {
        this.c = c;
        name = new ArrayList<String>();
        phonenumber = new ArrayList<String>();
    }

    @NonNull
    @Override
    public nameholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.single_chat_history , parent , false);

        return new nameholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull nameholder holder, final int position) {
        final String a = name.get(position);

        String m = a.substring(0 , 1);

        holder.circle.setText(m);
        holder.name.setText(a);

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(c , chat.class);
                i.putExtra("provider name" , a);
                i.putExtra("provider number" , phonenumber.get(position));
                c.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class nameholder extends RecyclerView.ViewHolder{

        TextView circle , name;

        public nameholder(@NonNull View itemView) {
            super(itemView);

            circle = (TextView)itemView.findViewById(R.id.circle);
            name = (TextView)itemView.findViewById(R.id.name);
        }
    }

    public void addname(String n , String p){
        name.add(n);
        phonenumber.add(p);
        notifyDataSetChanged();
    }
}
