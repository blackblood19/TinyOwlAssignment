package com.example.chaitanya.tinyowlassignment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * Created by Chaitanya on 9/27/2015.
 */
public class PostDialog extends DialogFragment{
    private String mAction;
    private String mMessage;

    /*PostDialog(String action){
        this.mAction = action;
    }*/

    public void setAction(String action){
        this.mAction = action;
    }


    public interface PostDialogListener{
        public void onDialogSubmit(DialogFragment dialog);
    }
    PostDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the PostDialogListener so we can send events to the host
            mListener = (PostDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.post_dialog, null);
        builder.setView(dialogView);

        Button btnAction = (Button) dialogView.findViewById(R.id.postAction);
        btnAction.setText(mAction);
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDialogSubmit(PostDialog.this);
                //btnAction.setText("ADD");
            }
        });

        // 2. Chain together various setter methods to set the dialog characteristics
        /*builder.setMessage("Dialog Message")
                .setTitle("Dialog Title");*/

        // 3. Get the AlertDialog from create()
        //AlertDialog dialog = builder.create();
        return builder.create();
    }
}
