package com.example.spice.ui.model_decision;
import android.content.Context;
import android.util.Log;
import com.example.spice.ml.Model;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.label.TensorLabel;

public class GenreClassifier {
    private Context context;
    Map<String, Float> floatMap = new HashMap<String, Float>();

    public GenreClassifier(Context context){
        this.context=context;
    }

    public Map<String, Float> predict(float data[]){
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


    public String getMaxProbabilityString(){
        float max = 0;
        String maxProbabilityString = new String();

        for (Map.Entry<String, Float> entry : floatMap.entrySet())
        {
            if(entry.getValue() > max){
                max = entry.getValue();
                maxProbabilityString = entry.getKey();
            }
        }
        return maxProbabilityString;
    }
}
