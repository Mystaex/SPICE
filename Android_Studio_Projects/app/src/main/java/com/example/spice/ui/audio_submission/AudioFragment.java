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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
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

    private int PERMISSION_CODE = 10;

    int recorded = 0;
    int recording = 0;
    Button btnRecord;
    Button btnPlay;
    Button btnDelete;
    Button btnSubmit;

    public static final int RequestPermissionCode = 1;

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
        btnDelete = v.findViewById(R.id.btnDelete);
        btnSubmit = v.findViewById(R.id.btnSubmit);

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(permission()){
                    setupMediaRecorder();

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
                    Toast.makeText(requireContext(), "Record!! Start function", Toast.LENGTH_SHORT).show();
                    btnDelete.setEnabled(true);
                    btnRecord.setEnabled(false);
                }
                else{
                    requestPermission();
                }
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)throws IllegalArgumentException,
                        SecurityException, IllegalStateException {

                    btnDelete.setEnabled(false);
                    String filepath = getActivity().getExternalFilesDir("/").getAbsolutePath();

                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(filepath + "/" + filename);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mediaPlayer.start();
                    Toast.makeText(getActivity(), "Recording Playing",
                            Toast.LENGTH_LONG).show();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(mediaRecorder != null){
                    try{
                        mediaRecorder.stop();
                    }
                    catch(IllegalStateException e){
                        e.printStackTrace();
                    }
                    mediaRecorder.release();
                    mediaRecorder = null;
                    btnDelete.setEnabled(false);
                    btnRecord.setEnabled(true);
                    btnPlay.setEnabled(true);
                    Toast.makeText(requireContext(), "Recording Completed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnSubmit.setOnClickListener(v1 -> { submit(); });

        btnSubmit.setEnabled(false);
        btnDelete.setEnabled(false);
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

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(getActivity(), "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(),"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
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
        Toast.makeText(requireContext(), "Record Start function", Toast.LENGTH_SHORT).show();
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