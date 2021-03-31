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

    private int PERMISSION_CODE = 1;
    private int minimumTimeMS = 5000;

    boolean recording = false;
    boolean playing = false;
    Button btnRecord;
    Button btnPlay;
    Button btnSubmit;

    private Chronometer timer;

    public static String filename = "audio.mp3";



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

        mediaRecorder = new MediaRecorder();

        btnRecord = v.findViewById(R.id.btnRecord);
        btnPlay = v.findViewById(R.id.btnPlay);
        btnSubmit = v.findViewById(R.id.btnSubmit);
        timer = v.findViewById(R.id.audioTimer);

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(!recording){
                    recording = true;
                    btnRecord.setText("Stop");
                    record();
                }
                else{
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
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)throws IllegalArgumentException, SecurityException, IllegalStateException {
                if(!playing){
                    playing = true;
                    btnPlay.setText("Stop");
                    play();
                }
                else{
                    stopPlay();
                }
            }
        });

        btnSubmit.setOnClickListener(v1 -> { submit(); });

        btnSubmit.setEnabled(false);
        btnPlay.setEnabled(false);

        return v;
    }


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

    public boolean permission() {
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_CODE);
            return false;
        }
    }

    private void record(){
        //: Implement Record functionality
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
            //Toast.makeText(requireContext(), "Recording", Toast.LENGTH_SHORT).show();
        }
    }
    private void play(){
        //: Implement Play functionality
        String filepath = getActivity().getExternalFilesDir("/").getAbsolutePath();

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filepath + "/" + filename);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();
        //Toast.makeText(getActivity(), "Playing", Toast.LENGTH_LONG).show();
    }

    private void stopRecord(){
        //: Implement Delete functionality
        timer.stop();

        if(mediaRecorder != null){
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            btnPlay.setEnabled(true);
            btnSubmit.setEnabled(true);
            //Toast.makeText(requireContext(), "Recording Completed", Toast.LENGTH_SHORT).show();
        }
    }

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
        //: Implement Submit functionality
        if(playing){
            stopPlay();
        }
        Toast.makeText(requireContext(), "Submit function", Toast.LENGTH_SHORT).show();
    }

}