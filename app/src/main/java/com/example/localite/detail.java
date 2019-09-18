package com.example.localite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

public class detail extends DialogFragment {

    String n , m  , t;
    Context c;

    double lat , lon;

    ProgressBar progressBar;
    Dialog loadingDialog;
    Dialog paymentMethodDialog;
    ImageButton patym,phonepe;

    SharedPreferences sharedPreferences;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view =  inflater.inflate(R.layout.detail , null);

        //TextView b = (TextView)view.findViewById(R.id.textView3);
        CardView chat = (CardView) view.findViewById(R.id.chat);
        CardView pay = (CardView)view.findViewById(R.id.pay);
        CardView navigate = (CardView) view.findViewById(R.id.nav);

        builder.setView(view);
        //b.setText(n);

        paymentMethodDialog = new Dialog(c);
        paymentMethodDialog.setContentView(R.layout.payment_method);
        paymentMethodDialog.setCancelable(true);
        patym = paymentMethodDialog.findViewById(R.id.
                patym);
        phonepe = paymentMethodDialog.findViewById(R.id.phonepe);

        //paymentMethodDialog.getWindow().setBackgroundDrawable(
                //new ColorDrawable(0));
        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.
                WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        patym = paymentMethodDialog.findViewById(R.id.
                patym);

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   Intent intent = new Intent(c , com.example.localite.chat.class);
                   intent.putExtra("provider name" , n);
                   intent.putExtra("provider number" , m);
                   c.startActivity(intent);
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //closefragment();
                paymentMethodDialog.show();
            }
        });

        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(c , com.example.localite.maps.class);
                Bundle b = new Bundle();
                b.putDouble("lat" , lat);
                b.putDouble("lon" , lon);
                i.putExtras(b);
                c.startActivity(i);
            }
        });

        patym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethodDialog.dismiss();
                try {
                    Intent i = c.getPackageManager().getLaunchIntentForPackage("net.one97.paytm");
                    startActivity(i);
                } catch (Exception e){
                    Toast.makeText(c,"Please Install Paytm Or Choose Other Option" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        phonepe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethodDialog.dismiss();
                try {
                    Intent i = c.getPackageManager().getLaunchIntentForPackage("com.phonepe.app");
                    startActivity(i);
                } catch (Exception e){
                    Toast.makeText(c,"Please Install Paytm Or Choose Other Option" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        return builder.create();
    }

    public void getName(String a , Context c , String b , String t ,  double lon , double lat){
        this.lat = lat;
        this.lon = lon;
        this.t = t;
        m = b;
        n = a;
        this.c = c;


    }
}
