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

    public OtherMessageAdapter(){

        omsg = new ArrayList<MessageClass>();
    }

    @NonNull
    @Override
    public OtherMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.omsg , parent , false);
        return new OtherMessageHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OtherMessageHolder holder, int position) {

        String a = omsg.get(position).getPromsg();
        holder.o.setText(a);

    }

    @Override
    public int getItemCount() {
        return omsg.size();
    }

    public class OtherMessageHolder extends RecyclerView.ViewHolder{

        TextView o;

        public OtherMessageHolder(@NonNull View itemView) {
            super(itemView);

            o = (TextView)itemView.findViewById(R.id.text);
        }
    }

    public void addmsg(MessageClass m1){
        omsg.add(m1);
        notifyDataSetChanged();
    }
}
