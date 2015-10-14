package com.example.chaitanya.tinyowlassignment;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
//import com.squareup.okhttp.OkHttpClient;
//import com.android.volley;

/**
 * Created by Chaitanya on 7/23/2015.
 */
public class Notes extends ActionBarActivity implements PostDialog.PostDialogListener{

    TextView txtAccessToken;
    ListView lstPosts;
    String strAccessToken;

    DatabaseAdapter databaseAdapter;

    // Instantiate the RequestQueue.
    RequestQueue queue;

    List<post> posts = new ArrayList<post>();
    postsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Log.d("ZMY", "Notes Activity OnCreate() ");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Up Button

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            strAccessToken = extras.getString("ACCESS_TOKEN");
        }

        databaseAdapter = new DatabaseAdapter(this);


        queue = Volley.newRequestQueue(this);
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


        //final ArrayAdapter<String> adapter;
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,posts);
        //adapter = new ArrayAdapter<String>(this, R.layout.posts_list_view, posts);
        adapter = new postsAdapter(this, posts);
        lstPosts = (ListView) findViewById(R.id.lstPosts);
        lstPosts.setAdapter(adapter);
        lstPosts.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView postsText = (TextView) view.findViewById(R.id.postsText);
                TextView postNodeId = (TextView) view.findViewById(R.id.postNodeId);
                Log.d("ZMY", "Clicked on " + position + ": " + postsText.getText().toString() + ", " + postNodeId.getText().toString());

                FragmentManager fragmentManager = getFragmentManager();
                PostDialog postDialog = new PostDialog();
                postDialog.setActionAndMessage("UPDATE", postsText.getText().toString(), postNodeId.getText().toString());
                postDialog.show(fragmentManager, "postDialog");
            }
        });

        getPostsFromDatabase();
        getPosts();


        adapter.setListener(new postsAdapter.postsAdapterListener() {
            @Override
            public void onDelClick(final int position) {
                String url = "https://graph.facebook.com/"+posts.get(position).getNodeId()+"?access_token="+strAccessToken;
                Log.d("ZMY", url);
                StringRequest booleanRequest = new StringRequest
                        (Request.Method.DELETE, url,new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("ZMY", response);
                                posts.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        }, new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("ZMY", "Error: " + error.toString());
                            }
                        });
                queue.add(booleanRequest);
            }
        });
    }


    @Override
    public void onDialogSubmit(final DialogFragment dialog) {
        final EditText postMessage = (EditText) dialog.getDialog().findViewById(R.id.postMessage);
        final TextView nodeId = (TextView) dialog.getDialog().findViewById(R.id.postNodeId);
        final Button btnAction = (Button) dialog.getDialog().findViewById(R.id.postAction);
        Log.d("ZMY", "Got the callback successfully. Message is " + postMessage.getText());
        if(nodeId.getText()==null || nodeId.getText().toString().isEmpty()){
            if(btnAction.getText().toString().equals("ADD")){
                addPost(postMessage.getText().toString());
            }
            else {
                Log.d("ZMY", "Node text is empty");
                return;
            }
        }
        else if(btnAction.getText().toString().equals("UPDATE")){
            editPost(nodeId.getText().toString(), postMessage.getText().toString());
        }
        dialog.dismiss();
    }

    private boolean updateList(String nodeId, String message) {
        for(int i=0;i<posts.size();i++){
            if(nodeId.equals(posts.get(i).getNodeId())){
                posts.set(i, new post(message, nodeId));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_notes, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.addPost:
                addPostDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addPostDialog() {
        Log.d("ZMY", "Adding a new post");
        FragmentManager fragmentManager = getFragmentManager();
        PostDialog postDialog = new PostDialog();
        postDialog.setActionAndMessage("ADD");
        postDialog.show(fragmentManager, "postDialog");
    }

    private void addPost(final String message){
        String encodedMessage="";
        try {
            encodedMessage = URLEncoder.encode(message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "https://graph.facebook.com/me/feed?message="+ encodedMessage +"&access_token="+strAccessToken;
        Log.d("ZMY", "Url is " + url);
        StringRequest jsonObjectRequest = new StringRequest
                (Request.Method.POST, url,new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ZMY", response);
                        getPosts();
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ZMY", "Error: " + error.toString());
                    }
                });
        queue.add(jsonObjectRequest);
    }

    private void editPost(final String nodeId, final String message) {
        String encodedMessage="";
        try {
            encodedMessage = URLEncoder.encode(message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "https://graph.facebook.com/"+nodeId+"?message="+ encodedMessage +"&access_token="+strAccessToken;
        Log.d("ZMY", "Url is " + url);
        StringRequest jsonObjectRequest = new StringRequest
                (Request.Method.POST, url,new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ZMY", response);
                        if(updateList(nodeId, message))
                            adapter.notifyDataSetChanged();
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ZMY", "Error: " + error.toString());
                    }
                });
        queue.add(jsonObjectRequest);
    }

    private void getPosts() {
        String url = "https://graph.facebook.com/me/posts?access_token="+strAccessToken;
        Log.d("ZMY", "URL is: " + url);

        posts.clear();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray data = response.optJSONArray("data");;
                        JSONObject datai;

                        for(int i=0;i<data.length();i++) {
                            datai = data.optJSONObject(i);
                            if (datai == null) break;
                            if (datai.opt("message") == null) continue;
                            posts.add(new post(datai.opt("message").toString(), datai.opt("id").toString()));
                            databaseAdapter.insertData(datai.opt("id").toString(), datai.opt("message").toString());
                            Log.d("ZMY", datai.opt("message").toString());
                        }
                        adapter.notifyDataSetChanged();
                        Log.d("ZMY", "should be updated");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ZMY", "Error response: " + error);
                    }
                });

        queue.add(jsObjRequest);
        // Access the RequestQueue through your singleton class.
        //MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
        //txtAccessToken.setText(strAccessToken);
    }

    private void getPostsFromDatabase() {
        Log.d("ZMY", "getPostsFromDatabase");
        List<post> postsFromDatabase = databaseAdapter.getPosts();
        posts.clear();
        posts.addAll(postsFromDatabase);
        posts.add(new post("Message", "nodeId"));
        adapter.notifyDataSetChanged();
    }
}
