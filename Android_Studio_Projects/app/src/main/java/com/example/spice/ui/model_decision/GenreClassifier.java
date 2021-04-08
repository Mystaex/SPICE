package com.example.spice.ui.model_decision;
import android.graphics.Bitmap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.modeldownloader.CustomModel;
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions;
import com.google.firebase.ml.modeldownloader.DownloadType;
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader;
import org.tensorflow.lite.Interpreter;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GenreClassifier {
    private Interpreter interpreter;
    private String[] labels = new String[]{"Blues", "Classical", "Country", "Disco", "Hip-Hop",
            "Jazz", "Metal", "Pop", "Reggae", "Rock"};

    public GenreClassifier(){
    }

    private String predict(float input[]){
        CustomModelDownloadConditions conditions = new CustomModelDownloadConditions.Builder()
                .requireWifi()  // Also possible: .requireCharging() and .requireDeviceIdle()
                .build();
        FirebaseModelDownloader.getInstance()
                .getModel("Genre-Classifier", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND, conditions)
                .addOnSuccessListener(new OnSuccessListener<CustomModel>() {
                    @Override
                    public void onSuccess(CustomModel model) {
                        File modelFile = model.getFile();
                        if (modelFile != null) {
                            Interpreter interpreter = new Interpreter(modelFile);
                        }
                    }
                });

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
