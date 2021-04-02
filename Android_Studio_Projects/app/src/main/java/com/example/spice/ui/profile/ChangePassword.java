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

    private EditText mPassword, mPasswordConfirm, mOld, mEmail;
    private Button changeBtn, backBtn;
    private FirebaseAuth auth;
    private String currentUserId;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference ref;
    private String retrievedPassword;
    private String retrievedEmail;
    private int flag = 0;
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
                String passwordValue = mPassword.getText().toString().trim();
                String passwordConfirmValue = mPasswordConfirm.getText().toString().trim();
                String oldpasswordValue = mOld.getText().toString().trim();
                String emailValue = mEmail.getText().toString().trim();
                flag = 0;

                //EMAIL EMPTY
                if(TextUtils.isEmpty(emailValue))
                {
                    flag--;
                    mEmail.setError("Email is required");
                }
                else
                {
                    flag++;
                }

                //EMAIL LESS THAN 6 CHARAS
                if(emailValue.length() < 6)
                {
                    flag--;
                    mEmail.setError("Email needs to be longer than 6 characters");
                }
                else
                {
                    flag++;
                }
                //EMAIL DOESN'T HAVE @ SYMBOL
                if(!emailValue.contains("@"))
                {
                    flag--;
                    mEmail.setError("Email needs an @ symbol");
                    return;
                }
                else
                {
                    flag++;
                }
                //EMAIL DOESN'T HAVE .COM
                if(!emailValue.contains(".com"))
                {
                    flag--;
                    mEmail.setError("Email needs to include .com");
                    return;
                }
                else
                {
                    flag++;
                }
                //EMAIL DOES NOT MATCH THE CURRENT EMAIL
                if (!retrievedEmail.equals(emailValue))
                {
                    flag--;
                    mEmail.setError("Email Incorrect");
                }
                else
                {
                    flag++;
                }
                //OLD PASSWORD EMPTY
                if (TextUtils.isEmpty(oldpasswordValue))
                {
                    flag--;
                    mOld.setError("Old password is required");
                    return;
                }
                else
                {
                    flag++;
                }

                //OLD PASSWORD LESS THAN 6 CHARAS
                if (oldpasswordValue.length() < 6)
                {
                    flag--;
                    mOld.setError("Old password needs to be longer than 6 characters");
                }
                else
                {
                    flag++;
                }

                //OLD PASSWORD AND CURRENT PASSWORD DON'T MATCH
                if (!retrievedPassword.equals(oldpasswordValue))
                {
                    flag--;
                    mOld.setError("Password Incorrect");
                }
                else
                {
                    flag++;
                }

                //EMPTY PASSWORD
                if (TextUtils.isEmpty(passwordValue))
                {
                    flag--;
                    mPassword.setError("Password is required");
                    return;
                }
                else
                {
                    flag++;
                }

                //EMPTY CONFIRM PASSWORD
                if (TextUtils.isEmpty(passwordConfirmValue))
                {
                    flag--;
                    mPasswordConfirm.setError("Passwords are not the same");
                    return;
                }
                else
                {
                    flag++;
                }

                //PASSWORD LESS THAN 6 CHARAS
                if (passwordValue.length() < 6)
                {
                    flag--;
                    mPassword.setError("Password must be longer than 6 characters");
                    return;
                }
                else
                {
                    flag++;
                }

                //CONFIRM PASSWORD LESS THAN 6 CHARAS
                if (passwordConfirmValue.length() < 6)
                {
                    flag--;
                    mPasswordConfirm.setError("Password must be longer than 6 characters");
                    return;
                }
                else
                {
                    flag++;
                }

                //PASSWORD AND CONFIRM PASSWORD NOT THE SAME
                if (!passwordValue.equals(passwordConfirmValue))
                {
                    flag--;
                    mPasswordConfirm.setError("Must be the same as the password");
                    return;
                }
                else
                {
                    flag++;
                }

                if(flag == 13)
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
