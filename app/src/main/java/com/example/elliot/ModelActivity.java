package com.example.elliot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.pytorch.IValue;
import org.pytorch.LiteModuleLoader;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;
import org.pytorch.MemoryFormat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.lang.reflect.Type;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


public class ModelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);

        // load food categories and the neural net
        List<String> classes = load_classes();
        try {
            Module module = load_model();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: call load_img() and classify() on button presses
    public List<String> load_classes(){
        String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "models/classes.json");
        Log.i("data", jsonFileString);

        Gson gson = new Gson();
        Type listStringType = new TypeToken<List<String>>(){ }.getType();
        List<String> classes = gson.fromJson(jsonFileString, listStringType);

        for(int i=0; i<classes.size(); i++){
            Log.i("Data", ">Item" + i + "\n" + classes.get(i));
        }

        return classes;
    }

    public Module load_model() throws IOException {
        Module module = Module.load("models/traced_resnet18_classifier.pt");
        return module;
    }

    public Bitmap load_img(String path) throws IOException {
        Bitmap img = BitmapFactory.decodeStream(getAssets().open(path));
        return img;
    }

    public String classify(Bitmap img, Module module, List<String> classes){
        // TODO: check if it is the same as opening with cv2!!!
        // forward image to net
        Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(img, TensorImageUtils.TORCHVISION_NORM_MEAN_RGB, TensorImageUtils.TORCHVISION_NORM_STD_RGB);
        Tensor outputTensor = module.forward(IValue.from(inputTensor)).toTensor();
        float[] scores = outputTensor.getDataAsFloatArray();

        // get argmax
        float maxScore = -Float.MAX_VALUE;
        int maxScoreIdx = -1;
        for(int i=0; i<scores.length; i++){
            if(scores[i] > maxScore){
                maxScoreIdx = i;
            }
        }
        String food = classes.get(maxScoreIdx);
        return food;
    }
}