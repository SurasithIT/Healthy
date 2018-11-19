package com.example.surasith.healthy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.surasith.healthy.post.PostFragment;
import com.example.surasith.healthy.sleep.SleepFragment;
import com.example.surasith.healthy.weight.WeightFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MenuFragment extends Fragment{
    ListView menuList;
    TextView eMail;
    FirebaseAuth user = FirebaseAuth.getInstance();
    ArrayList<String> menu = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //  map view with fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        menu.clear();
        menu.add("BMI");
        menu.add("Weight");
        menu.add("Sleep Time");
        menu.add("Post");
        menu.add("Sign out");

        ArrayAdapter<String> menuAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, menu);

        // get ListView from fragment_menu.xml
        menuList = getView().findViewById(R.id.menu_list);
        menuList.setAdapter(menuAdapter);
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("MENU", "Click on menu = " + menu.get(position));

                if(position <= 3){
                    Fragment fragments[] = {new BMIFragment(), new WeightFragment(), new SleepFragment(),new PostFragment()};
                    changeFragment(fragments[position]);
                } else {
                    user.signOut();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_view, new LoginFragment())
                            .commit();
                }

            }
        });

        eMail = getView().findViewById(R.id.menu_username);

        if(user.getCurrentUser() != null) eMail.setText(user.getCurrentUser().getEmail());
    }

    private void changeFragment(Fragment fragment){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_view, fragment)
                .addToBackStack(null)
                .commit();
    }
}
