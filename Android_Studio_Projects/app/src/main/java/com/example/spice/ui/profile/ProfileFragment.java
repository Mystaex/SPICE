package com.example.spice.ui.profile;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.spice.R;
import com.google.firebase.auth.FirebaseAuth;


public class ProfileFragment extends Fragment
{

    public ProfileFragment()
    {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance()
    {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        /*
        Button btnEdit = v.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(v1 ->
        {
            startActivity(new Intent(getActivity(), ManageAccount.class));
        });

         */
        return v;
    }
}