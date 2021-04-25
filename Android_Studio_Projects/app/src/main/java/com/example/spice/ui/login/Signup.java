package com.example.spice.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.spice.R;
import com.example.spice.ui.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity
{
    private static final String TAG = "EmailPassword";
    //Initializing text boxes and buttons
    private TextInputLayout mEmail, mPassword, mPasswordConfirm, mUtaid;
    Button signupBtn, backBtn;

    //initializing database variables
    private FirebaseAuth auth;
    DatabaseReference ref;
    String currentUserId;
    Member member;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        //initializing and connecting values to their respective text boxes.
        mEmail = findViewById(R.id.signup_email);
        mPassword = findViewById(R.id.signup_password);
        mPasswordConfirm= findViewById(R.id.signup_confirm);
        mUtaid = findViewById(R.id.signup_utaid);
        signupBtn = findViewById(R.id.signup_submit);
        backBtn = findViewById(R.id.signup_back_submit);

        auth = FirebaseAuth.getInstance();

        //BACK BUTTON CLICKED
        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
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
                String emailValue = mEmail.getEditText().getText().toString().trim();
                String passwordValue = mPassword.getEditText().getText().toString().trim();
                String passwordConfirmValue = mPasswordConfirm.getEditText().getText().toString().trim();
                String utaidValue = mUtaid.getEditText().getText().toString().trim();

                //setting all errors to be blank again before setting any more errors
                mEmail.setError(null);
                mPasswordConfirm.setError(null);
                mUtaid.setError(null);

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
                //EMAIL DOESN'T HAVE .COM
                if(!emailValue.contains(".com"))
                {
                    mEmail.setError("Email needs to include .com");
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
                //PASSWORD AND CONFIRM PASSWORD NOT THE SAME
                if(!passwordValue.equals(passwordConfirmValue))
                {
                    mPasswordConfirm.setError("Must be the same as the password");
                    return;
                }
                //EMPTY UTA ID
                if(TextUtils.isEmpty(utaidValue))
                {
                    mUtaid.setError("UTA ID Number required");
                    return;
                }
                //UTA ID LESS THAN OR GREATER THAN 10 CHARAS
                if(utaidValue.length() != 10)
                {
                    mUtaid.setError("ID Number must have a length of 10");
                    return;
                }
                //UTAID NOT DIGIT
                if(!isNumber(utaidValue))
                {
                    mUtaid.setError("ID Number must be numbers");
                    return;
                }

                createAccount(emailValue,passwordValue, utaidValue);
            }
        });
    }

    //Function to test if the string inputted into this function is a number or not.
    public static boolean isNumber(String str)
    {
        try
        {
            Double.parseDouble(str);
            return true;
        }
        catch(NumberFormatException e)
        {
            return false;
        }
    }

    //The function that will create an account if the user information passes the if statements above.
    private void createAccount(String email, String password, String utaid)
    {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        Member member = new Member();
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            //Get the user that has just added their information to authentication system
                            FirebaseUser user = auth.getCurrentUser();
                            currentUserId = user.getUid();

                            //set values in the object to the user's values
                            member.setutaid(utaid);
                            member.setpassword(password);
                            member.setemail(email);
                            member.setuserid(currentUserId);

                            //set all of the values in firebase realtime database
                            ref = FirebaseDatabase.getInstance().getReference().child("Member");
                            ref.child(currentUserId).setValue(member);

                            ref = FirebaseDatabase.getInstance().getReference().child("Member").child(currentUserId).child("Graph");

                            int i = 0;
                            for (i = 0; i < 10; i++)
                            {
                                switch(i) {
                                    case 0:
                                        ref.child("Blues").setValue(0);
                                        break;
                                    case 1:
                                        ref.child("Classical").setValue(0);
                                        break;
                                    case 2:
                                        ref.child("Country").setValue(0);
                                        break;
                                    case 3:
                                        ref.child("Disco").setValue(0);
                                        break;
                                    case 4:
                                        ref.child("Hip-Hop").setValue(0);
                                        break;
                                    case 5:
                                        ref.child("Jazz").setValue(0);
                                        break;
                                    case 6:
                                        ref.child("Metal").setValue(0);
                                        break;
                                    case 7:
                                        ref.child("Pop").setValue(0);
                                        break;
                                    case 8:
                                        ref.child("Reggae").setValue(0);
                                        break;
                                    case 9:
                                        ref.child("Rock").setValue(0);
                                        break;
                                    default:
                                        break;
                                }
                            }

                            updateUI(user);
                        }
                        else
                        {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }


    //sends the user to the next page or doesn't if their signup fails.
    private void updateUI(FirebaseUser user)
    {
        if(user != null)
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }
}