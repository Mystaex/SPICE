package com.example.spice.ui.audio_submission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.spice.R;

import java.io.File;
import java.io.IOException;


public class AudioFragment extends Fragment {


    public AudioFragment() { }

    MediaRecorder mediaRecorder;

    int recorded = 0;
    int recording = 0;
    Button btnRecord;
    Button btnPlay;
    Button btnDelete;
    Button btnSubmit;

    public static String subfile = "audio.3gp";
    String filename = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + subfile;


    public static AudioFragment newInstance() {
        return new AudioFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mediaRecorder = new MediaRecorder();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_audio, container, false);

        btnRecord = v.findViewById(R.id.btnRecord);
        btnPlay = v.findViewById(R.id.btnPlay);
        btnDelete = v.findViewById(R.id.btnDelete);
        btnSubmit = v.findViewById(R.id.btnSubmit);

        btnRecord.setOnClickListener(v1 -> { record(); });
        btnPlay.setOnClickListener(v1 -> { play(); });
        btnDelete.setOnClickListener(v1 -> { delete(); });
        btnSubmit.setOnClickListener(v1 -> { submit(); });

        btnSubmit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnPlay.setEnabled(false);

        return v;
    }

    private void record(){
        //: Implement Record functionality

        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 10);
        }
        else {
            if (recording == 0) {
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                mediaRecorder.setOutputFile(filename);
                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                btnDelete.setEnabled(true);
                btnPlay.setEnabled(true);
                btnSubmit.setEnabled(true);
                Toast.makeText(requireContext(), "Record Start function", Toast.LENGTH_SHORT).show();
                //recording = 1;
            } /*else {
                mediaRecorder.stop();
                mediaRecorder.release();
                Toast.makeText(requireContext(), "Record Stop function", Toast.LENGTH_SHORT).show();
                recording = 0;
            }*/
        }

    }
    private void play(){
        //: Implement Play functionality
        MediaPlayer mediaPlayer = new MediaPlayer();

        try{
            mediaPlayer.setDataSource(filename);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        Toast.makeText(requireContext(), "Play function", Toast.LENGTH_SHORT).show();
    }
    private void delete(){
        //: Implement Delete functionality

        mediaRecorder.stop();
        btnDelete.setEnabled(false);
        mediaRecorder.release();
        Toast.makeText(requireContext(), "Record Stop function", Toast.LENGTH_SHORT).show();
        //recording = 0;
        //oast.makeText(requireContext(), "Delete function", Toast.LENGTH_SHORT).show();

    }
    private void submit(){
        //: Implement Submit functionality
        Toast.makeText(requireContext(), "Submit function", Toast.LENGTH_SHORT).show();

    }

}