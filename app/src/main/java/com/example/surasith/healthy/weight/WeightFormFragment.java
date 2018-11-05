package com.example.surasith.healthy.weight;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.surasith.healthy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeightFormFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weight_form, container , false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onClickBackBtn();
        onClickSaveBtn();
        initOnSetDate();
    }

    void onClickBackBtn(){
        Button backBtn = getView().findViewById(R.id.weightForm_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToWeightFragment();
            }
        });
    }

    void onClickSaveBtn(){
        Button saveBtn = getView().findViewById(R.id.weightForm_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText _date = getView().findViewById(R.id.weightForm_date);
                EditText _weight = getView().findViewById(R.id.weightForm_weight);

                if(_date.getText().toString().isEmpty() || _weight.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Fill date or weight", Toast.LENGTH_SHORT).show();
                } else {
                    String _dateStr = _date.getText().toString();
                    float _weightFloat = Float.parseFloat(_weight.getText().toString());
                    Toast.makeText(getActivity(), "Your weight : " + _weightFloat, Toast.LENGTH_SHORT).show();
                    setObjectToFireBase(new Weight(_dateStr, _weightFloat, ""));
                }

            }
        });
    }

    void setObjectToFireBase(Weight weight){
        ArrayList<Weight> weights = (ArrayList<Weight>) getArguments().getSerializable("weight");
        if(!weights.isEmpty()){
            Weight previousWeight = weights.get(0);
            if(weight.getDate().compareTo(previousWeight.getDate()) > 0) {
                if (weight.getWeight() > previousWeight.getWeight()) weight.setStatus("UP");
                else if (weight.getWeight() < previousWeight.getWeight()) weight.setStatus("DOWN");
            } else {
                weight.setStatus(previousWeight.getStatus());
            }
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("myfitness")
                .document(auth.getUid())
                .collection("weight")
                .document(weight.getDate()).set(weight)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        goToWeightFragment();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void goToWeightFragment(){
        getFragmentManager().popBackStack();
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.main_view, new WeightFragment())
//                .addToBackStack(null)
//                .commit();
    }

    void initOnSetDate(){
        EditText _date = getView().findViewById(R.id.weightForm_date);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = sdf.format(new Date());
        _date.setText(currentDate);
    }
}
