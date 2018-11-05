package com.example.surasith.healthy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BMIFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_bmi,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button bmiCalculate = getView().findViewById(R.id.bmi_btn);
        bmiCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText _bmiHeight = getView().findViewById(R.id.bmi_height);
                EditText _bmiWeight = getView().findViewById(R.id.bmi_weight);
                TextView _bmi = getView().findViewById(R.id.bmi_show);

                if(_bmiHeight.getText().toString().isEmpty() || _bmiWeight.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"Please fill Height or Weight.",Toast.LENGTH_SHORT).show();
                } else {
                    float bmiHeight = Float.parseFloat(_bmiHeight.getText().toString());
                    float bmiWeight = Float.parseFloat(_bmiWeight.getText().toString());
                    bmiHeight = bmiHeight/100;
                    float BMI = bmiWeight/(bmiHeight*bmiHeight);
                    _bmi.setText(String.format("%.2f", BMI));
                }

            }
        });

        onClickBackBtn();
    }

    private void onClickBackBtn(){
        Button backBtn = getView().findViewById(R.id.bmi_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
//                getActivity().getSupportFragmentManager()
//                        .beginTransaction().replace(R.id.main_view, new MenuFragment())
//                        .addToBackStack(null)
//                        .commit();
            }
        });
    }
}
