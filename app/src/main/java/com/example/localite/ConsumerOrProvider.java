package com.example.localite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ConsumerOrProvider extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.select_type , null);

        RadioGroup r = (RadioGroup)v.findViewById(R.id.radiogroup);
        RadioButton btnCon = (RadioButton)v.findViewById(R.id.consumer);
        RadioButton btnPro = (RadioButton)v.findViewById(R.id.provider);

        builder.setView(v).setMessage("Who are you?");

        r.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = (RadioButton)radioGroup.findViewById(i);
                MainActivity main = (MainActivity)getActivity();
                switch(i){

                    case R.id.consumer:
                         main.getType("Consumer");
                         dismiss();
                         break;

                    case R.id.provider:
                        main.getType("Provider");
                        dismiss();
                        break;
                }
            }
        });



        return builder.create();
    }
}
