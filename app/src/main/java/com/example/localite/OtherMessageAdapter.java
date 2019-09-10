package com.example.localite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OtherMessageAdapter extends RecyclerView.Adapter<OtherMessageAdapter.OtherMessageHolder> {

    ArrayList<MessageClass> omsg;


    TextView mmsg , smsg;
    TextView day , timeS , timeR;

    public OtherMessageAdapter(ArrayList<MessageClass> omsg){

        this.omsg = omsg;
    }

    public class OtherMessageHolder extends RecyclerView.ViewHolder{

        public OtherMessageHolder(@NonNull View itemView) {
            super(itemView);

            mmsg = (TextView)itemView.findViewById(R.id.text);
            day = (TextView)itemView.findViewById(R.id.day);
            timeR = (TextView)itemView.findViewById(R.id.time);
            timeS = (TextView)itemView.findViewById(R.id.time2);
            smsg = (TextView)itemView.findViewById(R.id.text3);
        }
    }

    @NonNull
    @Override
    public OtherMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.mymsg , parent , false);
        return new OtherMessageHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OtherMessageHolder holder, int position) {

        MessageClass m = omsg.get(position);
        String a = m.getPromsg();

        if (a != null){
            smsg.setVisibility(View.INVISIBLE);
            timeS.setVisibility(View.INVISIBLE);
            mmsg.setText(a);
            day.setText(omsg.get(position).getDay());
            timeR.setText(omsg.get(position).getTime());
        }else{
            mmsg.setVisibility(View.INVISIBLE);
            timeR.setVisibility(View.INVISIBLE);
            smsg.setText(m.getConmsg());
            day.setText(omsg.get(position).getDay());
            timeS.setText(omsg.get(position).getTime());
        }


    }

    @Override
    public int getItemCount() {
        return omsg.size();
    }



    public void addmsg(MessageClass m1){
        omsg.add(m1);
        notifyDataSetChanged();
    }
}
