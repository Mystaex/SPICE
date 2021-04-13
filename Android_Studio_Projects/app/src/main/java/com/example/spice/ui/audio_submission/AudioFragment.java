/*
 * This fragment is utilizing the OmRecorder library to record audio in .wav format, which
 * is not natively supported by any default Android Studio library
 *
 * OmRecorder is licensed for commercial use under the Apache License:
 *
 *
 * Copyright 2017 Kailash Dabhi (Kingbull Technology)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.spice.ui.audio_submission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;
import com.example.spice.R;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import android.widget.TextView;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import omrecorder.AudioChunk;
import omrecorder.AudioRecordConfig;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.PullableSource;
import omrecorder.Recorder;
import com.example.spice.ui.model_decision.GenreClassifier;



public class AudioFragment extends Fragment {

    public AudioFragment() {
    }

    FFmpeg ffmpeg;

    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    Recorder recorder;

    private int PERMISSION_CODE = 1;        //Random positive integer meaning permission granted
    private long minimumTimeMS = 5000;      //Minimum length in ms of recorded audio

    boolean recording = false;              //Decider of recording button switch
    boolean playing = false;                //Decider of playing button switch
    Button btnRecord;
    Button btnPlay;
    Button btnSubmit;

    private Chronometer timer;              //Recording timer

    public static String filename = "audio.wav";        //Audio file will always be audio.ogg

    public static AudioFragment newInstance() {
        return new AudioFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_audio, container, false);
        btnRecord = v.findViewById(R.id.btnRecord);
        btnPlay = v.findViewById(R.id.btnPlay);
        btnSubmit = v.findViewById(R.id.btnSubmit);
        timer = v.findViewById(R.id.audioTimer);

        //Record button case
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Switch to implement 2 functions in 1 button
                if (!recording) {
                    recording = true;
                    btnRecord.setText("Stop");
                    record();
                } else {
                    //Minimum recording time is currently 5 seconds
                    //If the recording isn't 5 seconds long, let them know.
                    long recordTime = SystemClock.elapsedRealtime() - timer.getBase();

                    if (recordTime >= minimumTimeMS) {
                        recording = false;
                        btnRecord.setText("Record");
                        stopRecord();
                    } else {
                        Toast.makeText(requireContext(), "Recording needs 5 seconds minimum", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        //Play button case
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException, SecurityException, IllegalStateException {
                //Switch to implement 2 functions in 1 button
                if (!playing) {
                    playing = true;
                    btnPlay.setText("Stop");
                    play();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            stopPlay();
                        }
                    });
                } else {
                    //playing set and setText are in stopPlay() so that submit() can access.
                    stopPlay();
                }
            }
        });

        btnSubmit.setOnClickListener(v1 -> {
            submit();
        });
        btnSubmit.setEnabled(false);
        btnPlay.setEnabled(false);
        return v;
    }


    //Function that checks permissions for recording audio, and if not granted, requests them
    public boolean permission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_CODE);
            return false;
        }
    }


    //Function that implements the record functionality
    private void record() {
        if (permission()) {
            //Starting of timer
            timer.setBase(SystemClock.elapsedRealtime());
            timer.start();
            setupRecorder();
            recorder.startRecording();
            btnPlay.setEnabled(false);
            btnSubmit.setEnabled(false);
        }
    }

    private void setupRecorder() {
        recorder = OmRecorder.wav(
                new PullTransport.Default(mic(), new PullTransport.OnAudioChunkPulledListener() {
                    @Override
                    public void onAudioChunkPulled(AudioChunk audioChunk) {
                    }
                }), file());
    }

    private PullableSource mic() {
        return new PullableSource.Default(
                new AudioRecordConfig.Default(
                        MediaRecorder.AudioSource.MIC, AudioFormat.ENCODING_PCM_16BIT,
                        AudioFormat.CHANNEL_IN_MONO, 44100
                )
        );
    }

    private File file() {
        return new File(getActivity().getExternalFilesDir("/").getAbsolutePath(), filename);
    }

    //Function that implements the play audio functionality
    private void play() {
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
    private void stopRecord() {
        timer.stop();

        try {
            recorder.stopRecording();
        } catch (IOException e) {
            e.printStackTrace();
        }
        btnPlay.setEnabled(true);
        btnSubmit.setEnabled(true);
    }


    //Function that implements the functionality to stop the audio being played
    private void stopPlay() {
        playing = false;
        btnPlay.setText("Play");
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void submit() {
        if (playing) {
            stopPlay();

        }
        String filepath = getActivity().getExternalFilesDir("/").getAbsolutePath();

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(getActivity()));
        } // this will start python
        TextView textView;
        //create python instance
        Python py = Python.getInstance();
        //create python object
        PyObject pyobj = py.getModule("pythonScipt");
        // call the function
        float[] features = pyobj.callAttr("getArray").toJava(float[].class);

        GenreClassifier genreClassifier = new GenreClassifier(getContext());

        Map<String, Float> map = genreClassifier.predict(features);
        genreClassifier.getMaxProbabilityString();
        System.out.println(genreClassifier.getMaxProbabilityString());
    }
}