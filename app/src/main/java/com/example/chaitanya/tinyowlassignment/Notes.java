package com.example.chaitanya.tinyowlassignment;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.DownloadManager;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;
//import com.squareup.okhttp.OkHttpClient;
//import com.android.volley;

/**
 * Created by Chaitanya on 7/23/2015.
 */
public class Notes extends ActionBarActivity {

    TextView txtAccessToken;
    ListView lstPosts;
    String strAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Log.d("ZMY", "Notes Activity OnCreate() ");

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            strAccessToken = extras.getString("ACCESS_TOKEN");
        }

        txtAccessToken = (TextView) findViewById(R.id.txtAccessToken);


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://graph.facebook.com/me/posts?access_token="+strAccessToken;
        Log.d("ZMY", "URL is: " + url);

        /*StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("ZMY", "Response is: " + response.substring(0));
                        //mTextView.setText("Response is: " + response.substring(0, 500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ZMY", "That didn't work!");
                //mTextView.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);*/

        final List<String> posts = new ArrayList<String>();
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,posts);
        //adapter = new ArrayAdapter<String>(this, R.layout.posts_list_view, posts);
        lstPosts = (ListView) findViewById(R.id.lstPosts);
        lstPosts.setAdapter(adapter);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        /*JSONArray jsonArray;
                        response.toJSONArray(jsonArray);*/
                        JSONArray data = response.optJSONArray("data");;
                        JSONObject datai;

                        for(int i=0;i<data.length();i++) {
                            datai = data.optJSONObject(i);
                            if(datai==null) break;
                            if(datai.opt("message")==null)  continue;
                            posts.add(datai.opt("message").toString());
                            adapter.notifyDataSetChanged();
                            Log.d("ZMY", datai.opt("message").toString());
                        }
                        //txtAccessToken.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ZMY", "Error response: " + error);
                    }
                });

        queue.add(jsObjRequest);
        // Access the RequestQueue through your singleton class.
        //MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
        //txtAccessToken.setText(strAccessToken);
    }
}
