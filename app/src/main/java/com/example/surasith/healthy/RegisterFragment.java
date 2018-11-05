package com.example.surasith.healthy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_register, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRegisterBtn();
    }

    void initRegisterBtn(){
        Button registerBtn = getView().findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText _userID = getView().findViewById(R.id.regis_email);
                EditText _RePassword = getView().findViewById(R.id.regis_re_password);
                EditText _password = getView().findViewById(R.id.regis_password);

                String userIdStr = _userID.getText().toString();
                String passwordStr = _password.getText().toString();
                String re_passwordStr = _RePassword.getText().toString();


                if (userIdStr.isEmpty() || re_passwordStr.isEmpty() || passwordStr.isEmpty()){
                    Toast.makeText(getActivity(), "กรุณาระบุข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                }
                else if (passwordStr.equals(re_passwordStr) && passwordStr.length() >= 6) {
                    loading(true);
                    createAccount(userIdStr,passwordStr);
                } else if (!passwordStr.equals(re_passwordStr)) {
                    Toast.makeText(getActivity(), "Password not match", Toast.LENGTH_SHORT).show();
                    String error = getString(R.string.password_not_match);
                    _RePassword.setError(error);
                } else {
                    Toast.makeText(getActivity(), "Password length should be greater than 5", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    void createAccount(String user, String password) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(user, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d("Register", ""+authResult);
                FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
                sendVerifiedEmail(curUser);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading(false);
                Log.d("Register", "Fail : "+e);
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void sendVerifiedEmail(final FirebaseUser _user) {
        _user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener< Void >() {
            @Override public void onSuccess(Void aVoid) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                Toast.makeText(getActivity(), "Send Verified Email", Toast.LENGTH_SHORT).show();
                goToLoginFragment();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override public void onFailure(@NonNull Exception e) {
                loading(false);
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void loading(boolean isLoad){

        Button registerBtn = getView().findViewById(R.id.register_btn);

    }

    void showDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Verify your email");
        alertDialog.setMessage("Please check your email.");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        goToLoginFragment();
                    }
                });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                goToLoginFragment();
            }
        });
        alertDialog.show();
    }

    void goToLoginFragment(){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_view, new LoginFragment())
                .addToBackStack(null)
                .commit();
    }
}
