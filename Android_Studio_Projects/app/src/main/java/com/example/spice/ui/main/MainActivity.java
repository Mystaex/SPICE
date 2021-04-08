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
import com.example.spice.ui.profile.ChangePassword;
import com.example.spice.ui.profile.ManageAccount;
import com.example.spice.ui.profile.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ml.modeldownloader.CustomModel;
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions;
import com.google.firebase.ml.modeldownloader.DownloadType;
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader;
import org.tensorflow.lite.Interpreter;

import java.io.File;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener
{
    ColorStateList def;
    TextView item1, item2, item3, select;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(getString(R.string.main_label));

        item1 = findViewById(R.id.item1);
        item2 = findViewById(R.id.item2);
        item3 = findViewById(R.id.item3);

        item1.setOnClickListener(this);
        item2.setOnClickListener(this);
        item3.setOnClickListener(this);

        select = findViewById(R.id.select);
        def = item2.getTextColors();
    }


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

        if (item.getItemId() == R.id.item_logout)
        {
            //implements functionality when is logout
            FirebaseAuth.getInstance().signOut();
            finish();
            return true;
        }
        else if(item.getItemId() == R.id.item_password)
        {
            startActivity(new Intent(getApplicationContext(), ChangePassword.class));
            finish();
            return true;
        }
        else if(item.getItemId() == R.id.item_edit)
        {
            startActivity(new Intent(getApplicationContext(), ManageAccount.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        setFragment(AudioFragment.newInstance());
    }


    @Override
    protected void onResume()
    {
        super.onResume();
    }


    @Override
    protected void onRestart()
    {
        super.onRestart();
    }


    @Override
    protected void onPause()
    {
        super.onPause();
    }


    @Override
    protected void onStop()
    {
        super.onStop();
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }


    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.item1)
        {
            select.animate().x(0).setDuration(100);
            item1.setTextColor(Color.BLACK);
            item2.setTextColor(def);
            item3.setTextColor(def);

            setFragment(AudioFragment.newInstance());

        }
        else if (view.getId() == R.id.item2)
        {
            item1.setTextColor(def);
            item2.setTextColor(Color.BLACK);
            item3.setTextColor(def);
            int size = item2.getWidth();
            select.animate().x(size).setDuration(100);

            setFragment(GraphsFragment.newInstance());
        }
        else if (view.getId() == R.id.item3)
        {
            item1.setTextColor(def);
            item2.setTextColor(def);
            item3.setTextColor(Color.BLACK);
            int size = item2.getWidth() * 2;
            select.animate().x(size).setDuration(100);

            setFragment(ProfileFragment.newInstance());

        }
    }


    private void setFragment(Fragment fragment)
    {
        getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.base_container, fragment)
        .commit();
    }


    //getGroupId()
    @Override
    public boolean onMenuItemClick(MenuItem item)
    {
        return true;
    }
}