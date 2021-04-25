/** Information if needed. Here is full link: https://github.com/firebase/snippets-android/blob/8fa42b206795c271810b038687744b2d2ac15357/auth/app/src/main/java/com/google/firebase/quickstart/auth/EmailPasswordActivity.java#L36-L36
 * Copyright 2021 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.spice.ui.login;

import com.example.spice.ui.main.MainActivity;
import com.example.spice.R;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

//Class that holds all login capability
public class login extends AppCompatActivity
{
    private static final String TAG = "EmailPassword";
    
    //Variables TextInputLayout for the email and password textbox strings
    private TextInputLayout mEmail, mPassword;

    //Variables that hold the Submit and the Create Account buttons
    private Button loginBtn, createBtn;

    //The code used to create an instance of authentication so we can authenticate the user
    private FirebaseAuth auth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Here we get all of the variables and attach them to the objects in the login xml file
        auth = FirebaseAuth.getInstance();
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        loginBtn = findViewById(R.id.submit);
        createBtn = findViewById(R.id.signup_button);

        //If the user clicks the "Create New Account" option they will be sent to that page.
        createBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //User will be sent to the SignUp page.
                Toast.makeText(login.this, "Create New Account", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Signup.class));
            }
        });

        //If the user clicks the "submit" option to submit their password.
        loginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //The objects will be made into strings that we can test.
                String emailValue = mEmail.getEditText().getText().toString().trim();
                String passwordValue = mPassword.getEditText().getText().toString().trim();

                //setting all errors to be blank again before setting any more errors
                mEmail.setError(null);
                mPassword.setError(null);

                //EMAIL EMPTY
                if(TextUtils.isEmpty(emailValue))
                {
                    mEmail.setError("Email is required");
                    return;
                }
                //EMAIL LESS THAN 6 CHARAS
                else if(emailValue.length() < 6)
                {
                    mEmail.setError("needs to be more than 6 characters");
                    return;
                }
                //EMAIL DOESN'T HAVE @ SYMBOL
                else if(!emailValue.contains("@"))
                {
                    mEmail.setError("needs @ symbol");
                    return;
                }
                //EMAIL DOESN'T HAVE .COM
                else if(!emailValue.contains(".com"))
                {
                    mEmail.setError("must include .com");
                    return;
                }
                //PASSWORD EMPTY
                else if(TextUtils.isEmpty(passwordValue))
                {
                    mPassword.setError("Password is required");
                    return;
                }
                //PASSWORD LESS THAN 6 CHARAS
                else if(passwordValue.length() < 6)
                {
                    mPassword.setError("Password should be more than 6 characters");
                    return;
                }
                else
                {
                    signIn(emailValue, passwordValue);
                }
            }
        });
    }


    // Function to check if the user is already signed in
    @Override
    public void onStart()
    {
        super.onStart();
    }


    //Here, we are at the point where we know the email and password are correct when it comes to format
    //so we can now authenticate the passwords to see if they are incorrect in the database or not.
    private void signIn(String email, String password)
    {
        // Try to Login. If the user entered the wrong information it will say that the authentication failed
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Sign in success, updateUI and take user to the main page.
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                        }
                        else
                        {
                            // If sign in fails, display an error message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            mPassword.setError("Password may be incorrect");
                            mEmail.setError("Email may be incorrect");
                            updateUI(null);
                        }
                    }
                });
    }

    //If the user needs to have a verification email sent (may be used in the future if the application is
    //pushed further.
    private void sendEmailVerification()
    {
        // Send verification email
        final FirebaseUser user = auth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        // Email sent
                    }
                });
    }

    //This function will send the user to the main recording page if they pass all of the tests and are able to login.
    private void updateUI(FirebaseUser user)
    {
        if(user != null)
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }
}