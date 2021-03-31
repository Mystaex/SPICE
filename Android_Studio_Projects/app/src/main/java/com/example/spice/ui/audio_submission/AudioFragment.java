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
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.example.spice.R;

import java.io.File;
import java.io.IOException;

import android.content.Context;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;



public class AudioFragment extends Fragment {


    public AudioFragment() { }

    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    private int PERMISSION_CODE = 1;        //Random positive integer meaning permission granted
    private long minimumTimeMS = 5000;      //Minimum length in ms of recorded audio

    boolean recording = false;              //Decider of recording button switch
    boolean playing = false;                //Decider of playing button switch
    Button btnRecord;
    Button btnPlay;
    Button btnSubmit;

    private Chronometer timer;              //Recording timer

    public static String filename = "audio.mp3";        //Audio file will always be audio.mp3



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

        btnRecord = v.findViewById(R.id.btnRecord);
        btnPlay = v.findViewById(R.id.btnPlay);
        btnSubmit = v.findViewById(R.id.btnSubmit);
        timer = v.findViewById(R.id.audioTimer);


        //Record button case
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //Switch to implement 2 functions in 1 button
                if(!recording){
                    recording = true;
                    btnRecord.setText("Stop");
                    record();
                }
                else{
                    //Minimum recording time is currently 5 seconds
                    //If the recording isn't 5 seconds long, let them know.
                    long recordTime = SystemClock.elapsedRealtime() - timer.getBase();
                    if(recordTime >= minimumTimeMS){
                        recording = false;
                        btnRecord.setText("Record");
                        stopRecord();
                    }
                    else{
                        Toast.makeText(requireContext(), "Recording needs 5 seconds minimum" , Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        //Play button case
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)throws IllegalArgumentException, SecurityException, IllegalStateException {
                //Switch to implement 2 functions in 1 button
                if(!playing){
                    playing = true;
                    btnPlay.setText("Stop");
                    play();
                }
                else{
                    //playing set and setText are in stopPlay() so that submit() can access.
                    stopPlay();
                }
            }
        });

        btnSubmit.setOnClickListener(v1 -> { submit(); });

        btnSubmit.setEnabled(false);
        btnPlay.setEnabled(false);

        return v;
    }

    //All necessary settings for the mediarecorder
    public void setupMediaRecorder(){
        String filepath = getActivity().getExternalFilesDir("/").getAbsolutePath();
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setAudioEncodingBitRate(96000);
        mediaRecorder.setOutputFile(filepath + "/" + filename);
    }

    //Function that checks permissions for recording audio, and if not granted, requests them
    public boolean permission() {
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_CODE);
            return false;
        }
    }

    //Function that implements the record functionality
    private void record(){
        //Starting of timer
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();

        if(permission()){
            setupMediaRecorder();
            btnPlay.setEnabled(false);
            btnSubmit.setEnabled(false);

            try{
                mediaRecorder.prepare();
            }
            catch(IllegalStateException e){
                e.printStackTrace();
            }
            catch(IOException e){
                e.printStackTrace();
            }

            mediaRecorder.start();
        }
    }

    //Function that implements the play audio functionality
    private void play(){
        String filepath = getActivity().getExternalFilesDir("/").getAbsolutePath();

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filepath + "/" + filename);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();
    }

    //Functions that implements the functionality to stop recording audio
    private void stopRecord(){
        timer.stop();

        if(mediaRecorder != null){
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            btnPlay.setEnabled(true);
            btnSubmit.setEnabled(true);
        }
    }

    //Function that implements the functionality to stop the audio being played
    private void stopPlay(){
        playing = false;
        btnPlay.setText("Play");
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void submit(){
        if(playing){
            stopPlay();
        }
        //: Implement Submit functionality
        Toast.makeText(requireContext(), "Submit function", Toast.LENGTH_SHORT).show();
    }

}