package com.example.spice.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spice.ui.main.MainActivity;
import com.example.spice.R;

public class login extends AppCompatActivity {

    EditText mEmail, mPassword;
    Button loginBtn, createBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        loginBtn = findViewById(R.id.submit);
        createBtn = findViewById(R.id.signup_button);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(login.this, "Create New Account", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Signup.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String emailValue = mEmail.getText().toString().trim();
                String passwordValue = mPassword.getText().toString().trim();

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

                if(passwordValue.length() < 6)
                {
                    mPassword.setError("Password should be more than 6 characters");
                    return;
                }

                if(emailValue.equals("cse3310") && passwordValue.equals("1234567"))
                {
                    Toast.makeText(login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });
    }
}