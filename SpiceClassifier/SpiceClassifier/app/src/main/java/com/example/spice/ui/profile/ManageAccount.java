package com.example.spice.ui.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.spice.R;
import com.example.spice.ui.main.MainActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ManageAccount extends AppCompatActivity
{
    //Initializing text boxes and buttons
    private TextInputLayout mName, mProfession, mGenre;
    Button editBtn, backBtn;

    //initializing database
    private FirebaseAuth auth;
    private DatabaseReference ref;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        //initializing and connecting values to their respective text boxes.
        mName = findViewById(R.id.edit_name);
        mProfession = findViewById(R.id.edit_profession);
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
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        //SIGN UP BUTTON CLICKED
        editBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //After user clicks submit the strings are formatted how we want for validation
                String nameValue = mName.getEditText().getText().toString().trim();
                String professionValue = mProfession.getEditText().getText().toString().trim();
                String genreValue = mGenre.getEditText().getText().toString().trim();
                FirebaseUser user = auth.getCurrentUser();
                currentUserId = user.getUid();
                ref = FirebaseDatabase.getInstance().getReference().child("Member").child(currentUserId);

                //If the values are not empty we update them, but if they are empty we do not update anything.
                if(!TextUtils.isEmpty(nameValue))
                {
                    ref.child("name").setValue(nameValue);
                }
                if(!TextUtils.isEmpty(professionValue))
                {
                    ref.child("profession").setValue(professionValue);
                }
                if(!TextUtils.isEmpty(genreValue))
                {
                    ref.child("genre").setValue(genreValue);
                }

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }
}