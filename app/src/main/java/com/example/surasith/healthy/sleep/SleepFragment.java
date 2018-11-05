package com.example.surasith.healthy.sleep;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.surasith.healthy.R;
import com.example.surasith.healthy.dbhelper.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class SleepFragment extends Fragment {
    private static final String TAG = "Sleep Fragment";

    private RecyclerView sleepItemList;
    private SleepTimeAdapter sleepTimeAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<SleepTimeItem> sleepTimeItems;

    private DBHelper db;

    public SleepFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sleep, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        db = new DBHelper(getActivity());
        sleepTimeItems = new ArrayList<>();
        sleepTimeItems.addAll(db.getAllItem());

        Log.d(TAG, "rows "+ db.getCountRow());

        initRecyclerView();
        initAddSleepButton();
        initBackButton();
    }

    private void initRecyclerView(){
        sleepItemList = getActivity().findViewById(R.id.sleep_time_list);
        sleepItemList.setHasFixedSize(true);

        sleepTimeAdapter = new SleepTimeAdapter(getContext(), sleepTimeItems);
        layoutManager = new LinearLayoutManager(getContext());

        sleepItemList.setAdapter(sleepTimeAdapter);
        sleepItemList.setLayoutManager(layoutManager);

        sleepTimeAdapter.setOnItemClickListener(new SleepTimeAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                editItem(position);
            }
        });
    }

    private void initAddSleepButton() {
        Button addSleepButton = getActivity().findViewById(R.id.sleep_form_button);
        addSleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SleepFormActivity.class));
            }
        });
    }

    private void initBackButton(){
        Button backButton = getActivity().findViewById(R.id.sleep_form_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity()
                        .getSupportFragmentManager()
                        .popBackStack();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDataSet();
    }

    private void updateDataSet(){
        sleepTimeItems.clear();
        sleepTimeItems.addAll(db.getAllItem());
        sleepTimeAdapter.notifyDataSetChanged();
    }

    private void editItem(int position){
        SleepTimeItem items = sleepTimeItems.get(position);
        Intent intent = new Intent(getActivity(), SleepFormActivity.class);
        intent.putExtra("item", items);
        startActivity(intent);
    }
}
