package com.example.localite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.squareup.picasso.Picasso;

public class imageFragment extends DialogFragment {

    private ImageView iv;
    private String mImageUri;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view =  inflater.inflate(R.layout.imageview , null);

        iv = (ImageView)view.findViewById(R.id.iv);

        builder.setView(view);

        Picasso.get().load(mImageUri).fit().centerCrop().into(iv);

        return builder.create();
    }

    public void getUri(String m){
        mImageUri = m;
    }
}
