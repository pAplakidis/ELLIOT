package com.example.elliot;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.pytorch.IValue;
import org.pytorch.LiteModuleLoader;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;


public class NNModel {
    private final Context context;

    public NNModel(Context context) {
        this.context = context;
    }

    // TODO: call load_img() and classify() on button presses
    public List<String> load_classes() {
        String jsonFileString = Utils.getJsonFromAssets(context, "classes.json");
        // Log.i("data", jsonFileString);

        Gson gson = new Gson();
        Type listStringType = new TypeToken<List<String>>() {
        }.getType();
        List<String> classes = gson.fromJson(jsonFileString, listStringType);

        // for (int i = 0; i < classes.size(); i++) {
        // Log.i("Data", ">Item" + i + "\n" + classes.get(i));
        // }

        return classes;
    }

    // TODO: has problems with paths!!!
    public Module load_model() throws IOException, URISyntaxException {
        return LiteModuleLoader.load(Utils.assetFilePath(context, "resnet18_classifier.ptl"));
    }

    public Bitmap load_img(String path) throws IOException {
        return BitmapFactory.decodeStream(context.getAssets().open(path));
    }

    public String classify(Bitmap img, Module module, List<String> classes) {
        // TODO: check if it is the same as opening with cv2!!!
        // forward image to net
        Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(img, TensorImageUtils.TORCHVISION_NORM_MEAN_RGB, TensorImageUtils.TORCHVISION_NORM_STD_RGB);
        Tensor outputTensor = module.forward(IValue.from(inputTensor)).toTensor();
        float[] scores = outputTensor.getDataAsFloatArray();

        // get argmax
        float maxScore = -Float.MAX_VALUE;
        int maxScoreIdx = -1;
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] > maxScore) {
                maxScoreIdx = i;
            }
        }

        return classes.get(maxScoreIdx);
    }
}
