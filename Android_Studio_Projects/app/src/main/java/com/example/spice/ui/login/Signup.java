package com.example.spice.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spice.R;
import com.example.spice.ui.main.MainActivity;

public class Signup extends AppCompatActivity {

    EditText mEmail, mPassword, mPasswordConfirm, mGenre;
    Button signupBtn, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        mEmail = findViewById(R.id.signup_email);
        mPassword = findViewById(R.id.signup_password);
        mPasswordConfirm= findViewById(R.id.signup_confirm);
        signupBtn = findViewById(R.id.signup_submit);
        backBtn = findViewById(R.id.back_submit);
        mGenre = findViewById(R.id.signup_genre);

        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(Signup.this, "Back to Login Page", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), login.class));
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String emailValue = mEmail.getText().toString().trim();
                String passwordValue = mPassword.getText().toString().trim();
                String passwordConfirmValue = mPasswordConfirm.getText().toString().trim();
                String genreValue = mGenre.getText().toString().trim();
                /*

                 */
                if(TextUtils.isEmpty(emailValue))
                {
                    mEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(passwordValue))
                {
                    mPassword.setError("Password is required");
                    return;
                }
                if(TextUtils.isEmpty(passwordConfirmValue))
                {
                    mPasswordConfirm.setError("Passwords are not the same");
                    return;
                }

                if(!emailValue.contains("@"))
                {
                    mEmail.setError("Email needs an @ symbol");
                    return;
                }

                if(passwordValue.length() < 6)
                {
                    mPassword.setError("Password should be more than 6 characters");
                    return;
                }

                if(!passwordValue.equals(passwordConfirmValue))
                {
                    mPasswordConfirm.setError("Must be the same as the password");
                    return;
                }
                Toast.makeText(Signup.this, "Signed up successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}