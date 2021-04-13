package com.example.spice.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        mEmail = findViewById(R.id.change_email);
        mOld = findViewById(R.id.change_old);
        mPassword = findViewById(R.id.change_password);
        mPasswordConfirm = findViewById(R.id.change_confirm);
        changeBtn = findViewById(R.id.change_submit);
        backBtn = findViewById(R.id.change_back_submit);

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


        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(ChangePassword.this, "Back to Profile Page", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });


        changeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //After user clicks submit the strings are formatted how we want for validation
                String passwordValue = mPassword.getEditText().getText().toString().trim();
                String passwordConfirmValue = mPasswordConfirm.getEditText().getText().toString().trim();
                String oldpasswordValue = mOld.getEditText().getText().toString().trim();
                String emailValue = mEmail.getEditText().getText().toString().trim();

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
                //EMAIL DOES NOT MATCH THE CURRENT EMAIL
                else if (!retrievedEmail.equals(emailValue))
                {
                    mEmail.setError("Email Incorrect");
                    return;
                }
                //OLD PASSWORD EMPTY
                else if (TextUtils.isEmpty(oldpasswordValue))
                {
                    mOld.setError("Old password is required");
                    return;
                }

                //OLD PASSWORD LESS THAN 6 CHARAS
                else if (oldpasswordValue.length() < 6)
                {
                    mOld.setError("Old password needs to be longer than 6 characters");
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
                //EMPTY CONFIRM PASSWORD
                else if (TextUtils.isEmpty(passwordConfirmValue))
                {
                    mPasswordConfirm.setError("Passwords are not the same");
                    return;
                }
                //PASSWORD LESS THAN 6 CHARAS
                else if (passwordValue.length() < 6)
                {
                    mPassword.setError("Password must be longer than 6 characters");
                    return;
                }
                //CONFIRM PASSWORD LESS THAN 6 CHARAS
                else if (passwordConfirmValue.length() < 6)
                {
                    mPasswordConfirm.setError("Password must be longer than 6 characters");
                    return;
                }
                //PASSWORD AND CONFIRM PASSWORD NOT THE SAME
                else if (!passwordValue.equals(passwordConfirmValue))
                {
                    mPasswordConfirm.setError("Must be the same as the password");
                    return;
                }
                else
                {
                    // Get auth credentials from the user for re-authentication. The example below shows
                    // email and password credentials but there are multiple possible providers,
                    // such as GoogleAuthProvider or FacebookAuthProvider.
                    currentUserId = user.getUid();
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider.getCredential(retrievedEmail, retrievedPassword);

                    // Prompt the user to re-provide their sign-in credentials
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
                                        if (task.isSuccessful()) {
                                            ref = FirebaseDatabase.getInstance().getReference().child("Member").child(currentUserId);
                                            ref.child("password").setValue(passwordValue);
                                            Toast.makeText(ChangePassword.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), login.class));
                                            finish();
                                        }
                                        else
                                        {
                                            Toast.makeText(ChangePassword.this, "Error password not updated", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(ChangePassword.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
