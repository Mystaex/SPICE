package com.example.spice.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.spice.R;
import com.example.spice.ui.audio_submission.AudioFragment;
import com.example.spice.ui.graphs.GraphsFragment;
import com.example.spice.ui.login.login;
import com.example.spice.ui.model_info.ModelFragment;
import com.example.spice.ui.profile.ChangePassword;
import com.example.spice.ui.profile.ManageAccount;
import com.example.spice.ui.profile.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener
{
    //Here we initialize the tab variables that will be used for each fragment
    ColorStateList def;
    TextView tab1, tab2, tab3, tab4, select;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //using a toolbar and setting the toolbar as SPICE CLASSIFIER
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.main_label));
        //toolbar.setTitleTextColor(getResources().getColor(R.color.titleColor));
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);


        tab1 = findViewById(R.id.tab1);
        tab2 = findViewById(R.id.tab2);
        tab3 = findViewById(R.id.tab3);
        tab4 = findViewById(R.id.tab4);

        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);
        tab4.setOnClickListener(this);

        select = findViewById(R.id.select);
        def = tab2.getTextColors();
    }

    //Method that implements additional options menu
    //Initialize the contents of the fragment
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();

        //If the user chooses the "Logout" option in the 3-Dot Menu
        if (item.getItemId() == R.id.item_logout)
        {
            //implements functionality when is logout
            startActivity(new Intent(getApplicationContext(), login.class));
            FirebaseAuth.getInstance().signOut();
            finish(); // Terminates activity
            return true;
        }
        //If the user chooses the "change password" option in the 3-dot menu which will direct them to the specified page
        else if(item.getItemId() == R.id.item_password)
        {
            startActivity(new Intent(getApplicationContext(), ChangePassword.class));
            finish(); // Terminates activity
            return true;
        }
        //If the user chooses the "Edit Account" option in the 3-dot menu which will direct them to the specified page
        else if(item.getItemId() == R.id.item_edit)
        {
            startActivity(new Intent(getApplicationContext(), ManageAccount.class));
            finish(); // Terminates activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //This method is called after onCreate method, this is part of android lifecycle
    // Called when the Fragment is visible to the user.
    // here we call setFragment to set AudioFragment as the principal  fragment
    @Override
    protected void onStart()
    {
        super.onStart();
        setFragment(AudioFragment.newInstance());
    }


    //Functions for if the user exits the application and enters back into the application
    @Override
    protected void onResume()
    {
        super.onResume();
    }

    //Functions for if the user exits the application and enters back into the application
    @Override
    protected void onRestart()
    {
        super.onRestart();
    }

    //Functions for if the user exits the application and enters back into the application
    @Override
    protected void onPause()
    {
        super.onPause();
    }

    //Functions for if the user exits the application and enters back into the application
    @Override
    protected void onStop()
    {
        super.onStop();
    }

    //Functions for if the user exits the application and enters back into the application
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    //OnClickListener method, listen to see if one of the tabs was pressed
    @Override
    public void onClick(View view)
    {
        //The first tab is the Recording Page
        if (view.getId() == R.id.tab1)
        {
            select.animate().x(0).setDuration(100);
            tab1.setTextColor(Color.WHITE);
            tab2.setTextColor(def);
            tab3.setTextColor(def);
            tab4.setTextColor(def);

            setFragment(AudioFragment.newInstance());
        }
        //The second tab is the Graphing page
        else if (view.getId() == R.id.tab2)
        {
            tab1.setTextColor(def);
            tab2.setTextColor(Color.WHITE);
            tab3.setTextColor(def);
            tab4.setTextColor(def);
            int size = tab2.getWidth();
            select.animate().x(size).setDuration(100);

            setFragment(GraphsFragment.newInstance());
        }
        else if(view.getId() == R.id.tab3)
        {
            tab1.setTextColor(def);
            tab2.setTextColor(def);
            tab3.setTextColor(Color.WHITE);
            tab4.setTextColor(def);
            int size = tab2.getWidth();
            select.animate().x(size).setDuration(100);

            setFragment(ModelFragment.newInstance());
        }
        //The third tab is the profile page
        else if (view.getId() == R.id.tab4)
        {
            tab1.setTextColor(def);
            tab2.setTextColor(def);
            tab3.setTextColor(def);
            tab4.setTextColor(Color.WHITE);
            int size = tab2.getWidth() * 2;
            select.animate().x(size).setDuration(100);

            setFragment(ProfileFragment.newInstance());
        }
    }

    //This method change the fragment on fragment container
    // @param fragment Fragment to be loaded into mainActivity
    private void setFragment(Fragment fragment)
    {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.base_container, fragment)
                .commit();
    }


    //Makes sure that no other action can be done until we move on to the next fragment
    @Override
    public boolean onMenuItemClick(MenuItem item)
    {
        return true;
    }
}