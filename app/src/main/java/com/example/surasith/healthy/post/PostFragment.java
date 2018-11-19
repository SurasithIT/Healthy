package com.example.surasith.healthy.post;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.surasith.healthy.R;
import com.example.surasith.healthy.post.comment.CommentActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class PostFragment extends Fragment {
    private OkHttpClient client;

    private Button backButton;
    private ListView postList;

    private PostAdapter adapter;
    private ArrayList<Post> posts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        posts = new ArrayList<>();
        adapter = new PostAdapter(getActivity(), R.layout.post_item, posts);

        postList = getActivity().findViewById(R.id.post_list);
        backButton = getActivity().findViewById(R.id.post_back);

        postList.setAdapter(adapter);
        postList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post item = posts.get(position);
                openCommentActivity(item.getId());
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        getPost();
    }

    private void getPost() {
        client = new OkHttpClient();

        String url = "https://jsonplaceholder.typicode.com/posts";

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

                    getActivity().runOnUiThread(new Runnable() {
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
        Type type = new TypeToken<ArrayList<Post>>(){}.getType();
        ArrayList<Post> dataSet = new Gson().fromJson(json, type);

        posts.clear();
        posts.addAll(dataSet);
        adapter.notifyDataSetChanged();
    }

    private void openCommentActivity(int postId) {
        Intent intent = new Intent(getActivity(), CommentActivity.class);
        intent.putExtra("postId", postId);
        startActivity(intent);
    }
}
