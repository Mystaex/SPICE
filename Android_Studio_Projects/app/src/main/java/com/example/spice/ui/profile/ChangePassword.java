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
package com.example.spice.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.spice.R;
import com.example.spice.ui.login.login;
import com.example.spice.ui.main.MainActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


// change password
public class ChangePassword extends AppCompatActivity
{
    //Initializing variables for all of the textboxes, buttons and firebase objects.
    private TextInputLayout mEmail, mPassword, mPasswordConfirm, mOld;
    private Button changeBtn, backBtn;
    private FirebaseAuth auth;
    private String currentUserId;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference ref;
    private String retrievedPassword;
    private String retrievedEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //Setting the previously initialized variables to their respective objects in the xml file
        mEmail = findViewById(R.id.change_email);
        mOld = findViewById(R.id.change_old);
        mPassword = findViewById(R.id.change_password);
        mPasswordConfirm = findViewById(R.id.change_confirm);
        changeBtn = findViewById(R.id.change_submit);
        backBtn = findViewById(R.id.change_back_submit);

        //Retrieving email and password from the database to check for when "Submit" is clicked
        currentUserId = user.getUid();
        FirebaseDatabase.getInstance().getReference().child("Member").child(currentUserId)
        .addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                retrievedEmail = dataSnapshot.child("email").getValue().toString();
                retrievedPassword = dataSnapshot.child("password").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(ChangePassword.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
            }
        });

        //If the user wants to go back to the previous page they can click the "back" button to be sent to the Main Recording Page.
        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        //If the user wants to reset their password and press the "Submit" button, their information will be checked here.
        changeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //After user clicks submit the strings are formatted how we want for validation
                String passwordValue = mPassword.getEditText().getText().toString().trim();
                String passwordConfirmValue = mPasswordConfirm.getEditText().getText().toString().trim();
                String oldpasswordValue = mOld.getEditText().getText().toString().trim();
                String emailValue = mEmail.getEditText().getText().toString().trim();

                //EMAIL DOES NOT MATCH THE CURRENT EMAIL
                if (!retrievedEmail.equals(emailValue))
                {
                    mEmail.setError("Email Incorrect");
                    return;
                }
                //OLD PASSWORD AND CURRENT PASSWORD DON'T MATCH
                else if (!retrievedPassword.equals(oldpasswordValue))
                {
                    mOld.setError("Password Incorrect");
                    return;
                }
                //EMPTY PASSWORD
                else if (TextUtils.isEmpty(passwordValue))
                {
                    mPassword.setError("Password is required");
                    return;
                }
                //PASSWORD LESS THAN 6 CHARAS
                else if (passwordValue.length() < 6)
                {
                    mPassword.setError("Password must be longer than 6 characters");
                    return;
                }
                //PASSWORD AND CONFIRM PASSWORD NOT THE SAME
                else if (!passwordValue.equals(passwordConfirmValue))
                {
                    mPasswordConfirm.setError("Must be the same as the password");
                    return;
                }
                else //ELSE THE USER INFORMATION CAN BE CONFIRMED BY THE AUTHENTICATION SYSTEM
                {
                    //We get the user's online ID and check to make sure their input is correct email and password
                    currentUserId = user.getUid();
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider.getCredential(retrievedEmail, retrievedPassword);

                    //After the user enters the correct information and their authentication checks out we can update their information
                    //in this statement.
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                user.updatePassword(passwordValue).addOnCompleteListener(new OnCompleteListener<Void>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if (task.isSuccessful())
                                        {
                                            //If the user was able to update their password, we also change the value in the realtime database.
                                            ref = FirebaseDatabase.getInstance().getReference().child("Member").child(currentUserId);
                                            ref.child("password").setValue(passwordValue);
                                            Toast.makeText(ChangePassword.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), login.class));
                                            finish();
                                        }
                                        else
                                        {
                                            //Firebase may not have been connected so the password was not updated in the database.
                                            Toast.makeText(ChangePassword.this, "Error password not updated", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else
                            {
                                //Tell the user that the information they entered isn't correct as it was not able to pass the verification process.
                                mEmail.setError("Failed Authentication, Email may be incorrect");
                                mOld.setError("Failed Authentication, Old Password may be incorrect");
                            }
                        }
                    });
                }
            }
        });
    }
}
