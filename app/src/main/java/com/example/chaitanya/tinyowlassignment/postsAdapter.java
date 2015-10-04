package com.example.chaitanya.tinyowlassignment;

import android.app.AlertDialog;
import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Chaitanya on 9/25/2015.
 */
public class postsAdapter extends BaseAdapter implements ListAdapter{
    private List<post> mPosts;
    Context mContext;

    postsAdapter(Context c, List<post> posts) {
        this.mContext = c;
        this.mPosts = posts;
    }

    @Override
    public int getCount() {
        return mPosts.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.posts_list_view, parent, false);

        TextView textView = (TextView) row.findViewById(R.id.postsText);
        textView.setText(mPosts.get(position).getMessage());

        TextView postNodeId = (TextView) row.findViewById(R.id.postNodeId);
        postNodeId.setText(mPosts.get(position).getNodeId());

        Button btn = (Button) row.findViewById(R.id.postsDel);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ZMY", "Clicked on " + position + ": " + mPosts.get(position).getMessage() + ", Node id: " + mPosts.get(position).getNodeId());
            }
        });

        return row;
    }
}
