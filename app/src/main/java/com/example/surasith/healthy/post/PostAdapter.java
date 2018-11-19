package com.example.surasith.healthy.post;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.surasith.healthy.R;

import java.util.ArrayList;

public class PostAdapter extends ArrayAdapter<Post> {
    private Context mContext;
    private ArrayList<Post> mPosts;

    public PostAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Post> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mPosts = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);

        TextView id = view.findViewById(R.id.post_id);
        TextView body = view.findViewById(R.id.post_body);

        Post item = mPosts.get(position);
        id.setText(item.getId() + " : " + item.getTitle());
        body.setText(item.getBody());

        return view;
    }
}
