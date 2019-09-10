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

    MessageClass m = new MessageClass();

    TextView mmsg , smsg;
    TextView day , timeS , timeR;

    public MyMessageAdapter(ArrayList<MessageClass> mymsg){

        this.mymsg = mymsg;
    }


    public class MyMessageHolder extends RecyclerView.ViewHolder{


        public MyMessageHolder(@NonNull View itemView) {
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
    public MyMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.mymsg , parent , false);

        return new MyMessageHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyMessageHolder holder, int position) {

          m = mymsg.get(position);

          String gg = m.getConmsg();

          if (gg != null){
              smsg.setVisibility(View.INVISIBLE);
              timeS.setVisibility(View.INVISIBLE);
              mmsg.setText(gg);
              day.setText(m.getDay());
              timeR.setText(m.getTime());
          }else{
              mmsg.setVisibility(View.INVISIBLE);
              timeR.setVisibility(View.INVISIBLE);
              smsg.setText(m.getPromsg());
              day.setText(m.getDay());
              timeS.setText(m.getTime());
          }
    }

    @Override
    public int getItemCount() {
        return mymsg.size();
    }

}
