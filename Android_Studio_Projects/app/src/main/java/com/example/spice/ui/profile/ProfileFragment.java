package com.example.spice.ui.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.spice.R;
import com.example.spice.ui.login.Member;
import com.example.spice.ui.login.login;
import com.example.spice.ui.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//https://youtu.be/OhVQlU25ICw

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance() {
        return new ProfileFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        Button btnEdit = v.findViewById(R.id.btnEdit);

        Button btnLogout = v.findViewById(R.id.btnLogout);

        Button btnChangePassword = v.findViewById(R.id.btnChangePassword);

        btnChangePassword.setOnClickListener(v1 -> {

            startActivity(new Intent(getActivity(), ChangePassword.class));
            // Implement edit information functionality
        });

        btnLogout.setOnClickListener(v1 -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(), login.class));
            // Implement edit information functionality
        });

        btnEdit.setOnClickListener(v1 -> {
            startActivity(new Intent(getActivity(), ManageAccount.class));
            // Implement edit information functionality
        });
        return v;
    }
}