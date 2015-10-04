package com.example.chaitanya.tinyowlassignment;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Home extends ActionBarActivity implements View.OnClickListener, PostDialog.PostDialogListener{

    Button btnMyNotes;
    String strAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d("ZMY", "Home Activity OnCreate() ");

        btnMyNotes = (Button)findViewById(R.id.btnMyNotes);
        btnMyNotes.setOnClickListener(this);

        strAccessToken = "CAACEdEose0cBALwQIDRw9nMp7LxZAZCCtBscfofbi1N1tOUxOH4mFB2enDrrkvk2t4q2P7RDgIcNnGzzvAjbpNTb8RCYyLrW1qNKMoojBrKetZBXiI1ulUuD8nyX4ZBzLZAJoaL5ZCyJWJf4Irq1Wrr6sFZB4g2DcuDIZBLh4lIjDxo9ytJjm45k1BZCWxPw0LoUs9lmrH6sz2AZDZD";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnMyNotes){
            Log.d("ZMY", "Pressed for NOTES");

            /*FragmentManager fragmentManager = getFragmentManager();
            PostDialog postDialog = new PostDialog();
            postDialog.setAction("ADDDD");
            postDialog.show(fragmentManager, "postDialog");*/


            Intent intent = new Intent(this, Notes.class);
            intent.putExtra("ACCESS_TOKEN", strAccessToken);
            startActivity(intent);
        }
    }

    @Override
    public void onDialogSubmit(DialogFragment dialog) {
        EditText editText = (EditText) dialog.getDialog().findViewById(R.id.postMessage);
        Log.d("ZMY", "Got the callback successfully. Message is " + editText.getText());
    }
}
