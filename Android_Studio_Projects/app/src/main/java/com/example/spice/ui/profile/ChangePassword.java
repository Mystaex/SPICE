package com.example.spice.ui.profile;

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
import com.example.spice.ui.login.Member;
import com.example.spice.ui.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


// change password
public class ChangePassword extends AppCompatActivity {

    EditText mPassword, mPasswordConfirm, mOld;
    Button changeBtn, backBtn;
    FirebaseAuth auth;
    String currentUserId;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference ref;
    String retrievedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mOld = findViewById(R.id.change_old);
        mPassword = findViewById(R.id.change_password);
        mPasswordConfirm = findViewById(R.id.change_confirm);
        changeBtn = findViewById(R.id.change_submit);
        backBtn = findViewById(R.id.change_back_submit);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangePassword.this, "Back to Profile Page", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //After user clicks submit the strings are formatted how we want for validation
                String passwordValue = mPassword.getText().toString().trim();
                String passwordConfirmValue = mPasswordConfirm.getText().toString().trim();
                String oldpasswordValue = mOld.getText().toString().trim();

                //EMAIL EMPTY
                if (TextUtils.isEmpty(oldpasswordValue)) {
                    mOld.setError("Old password is required");
                    return;
                }
                if (oldpasswordValue.length() < 6) {
                    mOld.setError("Old password needs to be longer than 6 characters");
                }



                //PASSWORD INCORRECT
                if(!retrievedPassword.equals(passwordValue))
                {
                    mOld.setError("Password Incorrect");
                }

                //EMPTY PASSWORD
                if (TextUtils.isEmpty(passwordValue)) {
                    mPassword.setError("Password is required");
                    return;
                }
                //EMPTY CONFIRM PASSWORD
                if (TextUtils.isEmpty(passwordConfirmValue)) {
                    mPasswordConfirm.setError("Passwords are not the same");
                    return;
                }
                //PASSWORD LESS THAN 6 CHARAS
                if (passwordValue.length() < 6) {
                    mPassword.setError("Password must be longer than 6 characters");
                    return;
                }
                //CONFIRM PASSWORD LESS THAN 6 CHARAS
                if (passwordConfirmValue.length() < 6) {
                    mPasswordConfirm.setError("Password must be longer than 6 characters");
                    return;
                }
                //PASSWORD AND CONFIRM PASSWORD NOT THE SAME
                if (!passwordValue.equals(passwordConfirmValue)) {
                    mPasswordConfirm.setError("Must be the same as the password");
                    return;
                }

                if(user!=null)
                {
                    user.updatePassword(passwordValue).addOnSuccessListener(new OnSuccessListener<Void>()
                    {

                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            FirebaseUser user = auth.getCurrentUser();
                            currentUserId = user.getUid();
                            ref = FirebaseDatabase.getInstance().getReference().child("Member").child(currentUserId);
                            ref.child("password").setValue(passwordValue);
                        }
                    });
                    Toast.makeText(ChangePassword.this, "Successfully change Password", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else
                {
                    Toast.makeText(ChangePassword.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}