package com.example.chaitanya.tinyowlassignment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;


public class Home extends ActionBarActivity implements View.OnClickListener{

    Button btnMyNotes;
    String strAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d("ZMY", "Home Activity OnCreate() ");

        btnMyNotes = (Button)findViewById(R.id.btnMyNotes);
        btnMyNotes.setOnClickListener(this);

        strAccessToken = "CAACEdEose0cBAAkrwZBMyjWMNI17QwOqOQg9sCWD7CWxcn2TulEDaXGNKEq5JFsfegrpaoVuap0t3Q2mcbcGioaZCmmNVKiGBCFj0dnuVAYalzIoSCceugPLJuZAXPjfkBkTNcolclNQGZA75DXlHqZBJEUETHmiAKPxJXbRLVtyU8WMQ2HecZCF00C8khnEYy2UnCiBIz4wZDZD";
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

            Intent intent = new Intent(this, Notes.class);
            intent.putExtra("ACCESS_TOKEN", strAccessToken);
            startActivity(intent);
        }
    }
}
