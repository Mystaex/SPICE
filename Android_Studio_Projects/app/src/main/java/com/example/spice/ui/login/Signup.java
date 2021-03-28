package com.example.spice.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spice.R;
import com.example.spice.ui.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signup extends AppCompatActivity {

    //Initializing text boxes and buttons
    EditText mEmail, mPassword, mPasswordConfirm, mGenre;
    Button signupBtn, backBtn;

    private FirebaseAuth auth;

    //initializing database
    //---DatabaseReference ref;
    //---Member member;
    //---long maxid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        //initializing and connecting values to their respective text boxes.
        mEmail = findViewById(R.id.signup_email);
        mPassword = findViewById(R.id.signup_password);
        mPasswordConfirm= findViewById(R.id.signup_confirm);
        mGenre = findViewById(R.id.signup_genre);
        signupBtn = findViewById(R.id.signup_submit);
        backBtn = findViewById(R.id.back_submit);

        auth = FirebaseAuth.getInstance();
        //---ref = FirebaseDatabase.getInstance().getReference().child("Member");
        //---member = new Member();

        /*---
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
        {
            if(dataSnapshot.exists())
            {
                maxid = (dataSnapshot.getChildrenCount());
            }
        }
         ---*/
        //BACK BUTTON CLICKED
        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(Signup.this, "Back to Login Page", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), login.class));
            }
        });

        //SIGN UP BUTTON CLICKED
        signupBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //After user clicks submit the strings are formatted how we want for validation
                String emailValue = mEmail.getText().toString().trim();
                String passwordValue = mPassword.getText().toString().trim();
                String passwordConfirmValue = mPasswordConfirm.getText().toString().trim();
                String genreValue = mGenre.getText().toString().trim();

                //EMPTY EMAIL
                if(TextUtils.isEmpty(emailValue))
                {
                    mEmail.setError("Email is required");
                    return;
                }
                //EMAIL DOESN'T HAVE @ SYMBOL
                if(!emailValue.contains("@"))
                {
                    mEmail.setError("Email needs an @ symbol");
                    return;
                }
                if(emailValue.length() < 6)
                {
                    mEmail.setError("Email needs to be longer than 6 characters");
                }
                //EMPTY PASSWORD
                if(TextUtils.isEmpty(passwordValue))
                {
                    mPassword.setError("Password is required");
                    return;
                }
                //EMPTY CONFIRM PASSWORD
                if(TextUtils.isEmpty(passwordConfirmValue))
                {
                    mPasswordConfirm.setError("Passwords are not the same");
                    return;
                }
                //PASSWORD LESS THAN 6 CHARAS
                if(passwordValue.length() < 6)
                {
                    mPassword.setError("Password must be longer than 6 characters");
                    return;
                }
                //CONFIRM PASSWORD LESS THAN 6 CHARAS
                if(passwordConfirmValue.length() < 6)
                {
                    mPasswordConfirm.setError("Password must be longer than 6 characters");
                    return;
                }
                //PASSWORD AND CONFIRM PASSWORD NOT THE SAME
                if(!passwordValue.equals(passwordConfirmValue))
                {
                    mPasswordConfirm.setError("Must be the same as the password");
                    return;
                }

                createAccount(emailValue,passwordValue);
                //---member.setemail(emailValue);
                //---member.setpassword(passwordValue);
                //---member.setgenre(genreValue);
                //---ref.push().setValue(member);
                //---ref.child(String.valueOf(maxid+1)).setValue(member);
            }
        });
    }
    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(null, "createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(null, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void updateUI(FirebaseUser user) {
        if(user != null)
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }
}