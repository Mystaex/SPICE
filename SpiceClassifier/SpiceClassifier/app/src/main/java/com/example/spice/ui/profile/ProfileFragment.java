package com.example.spice.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.spice.R;
import com.example.spice.ui.login.Member;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    private TextView name, utaId, profession, favoriteGenre, email;
    private FirebaseAuth mAuth;

    //This a fragment constructor
    public ProfileFragment() { }

    //This is a static method that returns a new instance of the fragment
    public static ProfileFragment newInstance() {
        return new ProfileFragment();

    }

    //Initialize fragment with lifecycle
    //Called to have the fragment instantiate its user interface view.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    //Creates and returns the view hierarchy associated with the fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        name = v.findViewById(R.id.tv_profileName);
        utaId = v.findViewById(R.id.tv_utaId);
        profession = v.findViewById(R.id.tv_profileProfession);
        favoriteGenre = v.findViewById(R.id.tv_profileFavoriteGenre);
        email = v.findViewById(R.id.tv_profileEmailAddress);

        return v;
    }

    // Method called after onCreateView
    // This method retrieves all  user details from database in firebase
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Initialize FirebaseDatabase instance (remote database)
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Get user uid locally saved
        String currentId = mAuth.getUid();

        //We need to search on firebase through database path
        DatabaseReference databaseReference = database.getReference().child("Member").child(currentId);

        //Add a value listener from firebase database to get realtime updates
        databaseReference.addValueEventListener(new ValueEventListener() {

            //This method returns snapshot of database path or reference previously called
            //This snapshot contains some type of data, we need to cast (convert) to own class (pojo)
            //(Plain Old Java Object)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Casting snapshot
                Member user = snapshot.getValue(Member.class);

                //Passing values to UI layout from model previously casted applying gettter methods
                name.setText(user.getname());
                email.setText(user.getemail());
                profession.setText(user.getprofession());
                utaId.setText(user.getutaid());
                favoriteGenre.setText(user.getgenre());

            }

            //This method will be triggered in the event that this listener either failed at the server, or
            // is removed as a result of the security and Firebase Database rules
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}