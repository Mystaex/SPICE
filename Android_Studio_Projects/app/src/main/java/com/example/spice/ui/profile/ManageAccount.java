package com.example.spice.ui.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spice.R;
import com.example.spice.ui.login.Member;
import com.example.spice.ui.main.MainActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Edit Account: startActivity(new Intent(getActivity(), ManageAccount.class));
//Sign Out: FirebaseAuth.getInstance().signOut();
//startActivity(new Intent(getActivity(), login.class));

public class ManageAccount extends AppCompatActivity {


    //Initializing text boxes and buttons
    EditText mName, mProfession, mUtaid, mGenre;
    Button editBtn, backBtn;

    //initializing database
    private FirebaseAuth auth;
    private DatabaseReference ref;
    private String currentUserId;
    Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        //initializing and connecting values to their respective text boxes.
        mName = findViewById(R.id.edit_name);
        mProfession = findViewById(R.id.edit_profession);
        mUtaid = findViewById(R.id.edit_utaid);
        mGenre = findViewById(R.id.edit_genre);
        editBtn = findViewById(R.id.edit_submit);
        backBtn = findViewById(R.id.edit_back_submit);

        auth = FirebaseAuth.getInstance();

        //BACK BUTTON CLICKED
        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(ManageAccount.this, "Back Profile Page", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        //SIGN UP BUTTON CLICKED
        editBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //After user clicks submit the strings are formatted how we want for validation
                String nameValue = mName.getText().toString().trim();
                String professionValue = mProfession.getText().toString().trim();
                String utaidValue = mUtaid.getText().toString().trim();
                String genreValue = mGenre.getText().toString().trim();

                member = new Member();

                FirebaseUser user = auth.getCurrentUser();
                currentUserId = user.getUid();
                ref = FirebaseDatabase.getInstance().getReference().child("Member").child(currentUserId);
                ref.child("name").setValue(nameValue);
                ref.child("profession").setValue(professionValue);
                ref.child("utaid").setValue(utaidValue);
                ref.child("genre").setValue(genreValue);
                Toast.makeText(ManageAccount.this, "Account Edited", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}