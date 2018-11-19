package com.example.surasith.healthy.post.comment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.surasith.healthy.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {
    private ListView commentListView;
    private CommentAdapter adapter;
    private ArrayList<Comment> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Comments");
        }

        comments = new ArrayList<>();
        adapter = new CommentAdapter(this, R.layout.comment_item, comments);

        commentListView = findViewById(R.id.comment_list_view);
        commentListView.setAdapter(adapter);

        int postId = getIntent().getIntExtra("postId", -1);

        if(postId > -1) getComments(postId);
    }

    private void getComments(int postId) {
        OkHttpClient client = new OkHttpClient();

        String url = "https://jsonplaceholder.typicode.com/posts/" + postId + "/comments";

        final Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String jsonData = response.body().string();

                    CommentActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            jsonToObject(jsonData);
                        }
                    });
                }
            }
        });
    }

    private void jsonToObject(String json) {
        Type type = new TypeToken<ArrayList<Comment>>(){}.getType();
        ArrayList<Comment> dataSet = new Gson().fromJson(json, type);

        comments.clear();
        comments.addAll(dataSet);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
