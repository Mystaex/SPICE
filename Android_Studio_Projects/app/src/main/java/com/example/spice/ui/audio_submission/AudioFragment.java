package com.example.spice.ui.audio_submission;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.spice.R;
import com.example.spice.ui.login.login;
import com.example.spice.ui.profile.ManageAccount;
import com.example.spice.ui.profile.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;

////Initialize e1
//EditText e1 = (EditText) findViewById(R.id.input);
//
//// Take text from e1 and assign it to str
//String str = e1.getText().toString();
//
////Initialize out
//TextView out= (TextView) findViewById(R.id.output);
//
//// Set text in str to EditText out
//out.setText(str);
public class AudioFragment extends Fragment {
    
    public AudioFragment() { }


    public static AudioFragment newInstance() {
        return new AudioFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_audio, container, false);

        Button btnRecord = v.findViewById(R.id.btnRecord);
        Button btnPlay = v.findViewById(R.id.btnPlay);
        Button btnDelete = v.findViewById(R.id.btnDelete);
        Button btnSubmit = v.findViewById(R.id.btnSubmit);

        btnRecord.setOnClickListener(v1 -> { record(); });
        btnPlay.setOnClickListener(v1 -> { play(); });
        btnDelete.setOnClickListener(v1 -> { delete(); });
        btnSubmit.setOnClickListener(v1 -> { submit(); });

        return v;
    }
    private void record(){
        //: Implement Record functionality
        Toast.makeText(requireContext(), "Record function", Toast.LENGTH_SHORT).show();
    }
    private void play(){
        //: Implement Play functionality
        Toast.makeText(requireContext(), "Play function", Toast.LENGTH_SHORT).show();
    }
    private void delete(){
        //: Implement Delete functionality
        Toast.makeText(requireContext(), "Delete function", Toast.LENGTH_SHORT).show();

    }
    private void submit(){
        //: Implement Submit functionality
        Toast.makeText(requireContext(), "Submit function", Toast.LENGTH_SHORT).show();

    }
}