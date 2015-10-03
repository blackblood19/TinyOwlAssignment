package com.example.chaitanya.tinyowlassignment;

import android.app.DialogFragment;
import android.app.FragmentManager;
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

        strAccessToken = "CAACEdEose0cBAEgA8VZCHM1UBcIZBmwZA3S0Rb4HpyqlhY4k0ordQZBTz2oYidXM3CqLTe20HDTlfQZCG46tl2PuaGCgUAnW2PBLYb7RcGmBjt1jaRZAgfkNfIQn96RsZBjkZC48ujmooZCR2WJ72Q6fIrYZC1XXfSKeThK2QZACEcPhtD2UaExfHsYcmEw5SNqmQDT3pEnVGZA27QZDZD";
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

            FragmentManager fragmentManager = getFragmentManager();
            PostDialog postDialog = new PostDialog();
            postDialog.show(fragmentManager, "postDialog");

            /*Intent intent = new Intent(this, Notes.class);
            intent.putExtra("ACCESS_TOKEN", strAccessToken);
            startActivity(intent);*/
        }
    }

    @Override
    public void onDialogSubmit(DialogFragment dialog) {
        EditText editText = (EditText) dialog.getDialog().findViewById(R.id.postMessage);
        Log.d("ZMY", "Got the callback successfully. Message is " + editText.getText());
    }
}
