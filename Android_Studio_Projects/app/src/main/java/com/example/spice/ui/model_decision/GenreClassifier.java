package com.example.spice.ui.model_decision;
import android.content.Context;
import android.util.Log;
import com.example.spice.ml.Model;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.ops.DequantizeOp;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.label.TensorLabel;

public class GenreClassifier {
    private Interpreter.Options options = new Interpreter.Options();
    private Context context;

    public GenreClassifier(Context context){
        this.context=context;
    }

    public Map<String, Float> predict(float data[]){
        Map<String, Float> floatMap = new HashMap<String, Float>();
        try {
            Model model = Model.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 17}, DataType.FLOAT32);
            inputFeature0.loadArray(data);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            final String ASSOCIATED_AXIS_LABELS = "labels.txt";
            List<String> associatedAxisLabels = null;

            try {
                associatedAxisLabels = FileUtil.loadLabels(context, ASSOCIATED_AXIS_LABELS);
            } catch (IOException e) {
                Log.e("tfliteSupport", "Error reading label file", e);
            }

            if (null != associatedAxisLabels) {
                // Map of labels and their corresponding probability
                TensorLabel labels = new TensorLabel(associatedAxisLabels,
                       outputFeature0);

                // Create a map to access the result based on label
                floatMap = labels.getMapWithFloatValue();
            }

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }

        return floatMap;
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
