package com.example.localite;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyMessageAdapter extends RecyclerView.Adapter<MyMessageAdapter.MyMessageHolder> {

    ArrayList<MessageClass> mymsg;

    public MyMessageAdapter(){

        mymsg = new ArrayList<MessageClass>();
    }
    @NonNull
    @Override
    public MyMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.mymsg , parent , false);

        return new MyMessageHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyMessageHolder holder, int position) {

        if (!mymsg.get(position).getConmsg().isEmpty()){
            String a = mymsg.get(position).getConmsg();
            holder.mmsg.setText(a);
            holder.day.setText(mymsg.get(position).getDay());
            holder.time.setText(mymsg.get(position).getTime());
        }else{
            String a = mymsg.get(position).getPromsg();
            holder.mmsg.setText(a);
            holder.day.setText(mymsg.get(position).getDay());
            holder.time.setText(mymsg.get(position).getTime());
        }

    }

    @Override
    public int getItemCount() {
        return mymsg.size();
    }

    public class MyMessageHolder extends RecyclerView.ViewHolder{

        TextView mmsg;
        TextView day , time;

        public MyMessageHolder(@NonNull View itemView) {
            super(itemView);

            mmsg = (TextView)itemView.findViewById(R.id.text);
            day = (TextView)itemView.findViewById(R.id.day);
            time = (TextView)itemView.findViewById(R.id.time);
        }
    }

    public void addmsg(MessageClass m1){
        mymsg.add(m1);
        notifyDataSetChanged();
    }
}
