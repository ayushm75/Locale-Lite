package com.example.localite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class detail extends DialogFragment {

    String n;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view =  inflater.inflate(R.layout.detail , null);

        TextView b = (TextView)view.findViewById(R.id.textView3);
        Button chat = (Button)view.findViewById(R.id.chat);
        Button pay = (Button)view.findViewById(R.id.pay);
        Button navigate = (Button)view.findViewById(R.id.navigate);

        builder.setView(view);

        b.setText(n);

        return builder.create();
    }

    public void getName(String a){
        n = a;
    }
}
