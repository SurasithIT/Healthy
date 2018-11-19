package com.example.surasith.healthy.post.comment;

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

public class CommentAdapter extends ArrayAdapter<Comment> {
    private Context mContext;
    private ArrayList<Comment> mComments;

    public CommentAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Comment> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mComments = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false);

        TextView id = view.findViewById(R.id.comment_id);
        TextView body = view.findViewById(R.id.comment_body);
        TextView name = view.findViewById(R.id.comment_name);
        TextView email = view.findViewById(R.id.comment_email);

        Comment item = mComments.get(position);
        id.setText(item.getPostId() + " : " + item.getId());
        body.setText(item.getBody());
        name.setText("name: " + item.getName());
        email.setText("E-mail: " + item.getEmail());

        return view;
    }
}
