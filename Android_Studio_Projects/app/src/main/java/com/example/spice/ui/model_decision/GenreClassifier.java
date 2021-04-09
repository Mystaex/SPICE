package com.example.spice.ui.model_decision;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;


import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class GenreClassifier {
    private Interpreter interpreter;
    private Interpreter.Options options = new Interpreter.Options();

    private String[] labels = new String[]{"Blues", "Classical", "Country", "Disco", "Hip-Hop",
            "Jazz", "Metal", "Pop", "Reggae", "Rock"};

    public GenreClassifier(){
        try{
            FileInputStream f_input_stream = new FileInputStream(new File("/src/main/assets/model.tflite"));
            FileChannel f_channel = f_input_stream.getChannel();
            MappedByteBuffer tflite_model = f_channel.map(FileChannel.MapMode.READ_ONLY, 0, f_channel.size());
            interpreter = new Interpreter(tflite_model, options);
        } catch (Exception e){
            Log.e("tfliteException", "Error: couldn't load tflite model.", e);
        }
    }

    public String predict(Double input[]){
        int bufferSize = 10 * Float.SIZE / Byte.SIZE;

        ByteBuffer modelOutput = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder());
        interpreter.run(input, modelOutput);

        modelOutput.rewind();
        FloatBuffer probabilities = modelOutput.asFloatBuffer();

        return labels[maxIndex(probabilities)];
    }


    private int maxIndex(FloatBuffer array){
        float max = array.get(0);
        int index = 0;

        for (int i = 0; i < array.capacity(); i++)
        {
            if (max < array.get(i))
            {
                max = array.get(i);
                index = i;
            }
        }
        return index;
    }
}
