package com.example.elliot;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.imgproc.Imgproc;

import org.pytorch.IValue;
import org.pytorch.LiteModuleLoader;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;



import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.List;

public class NNModel {
    private final Context context;

    public NNModel(Context context) {
        this.context = context;
    }

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

    public Bitmap load_img(String path) throws IOException {
        return BitmapFactory.decodeStream(context.getAssets().open(path));
    }

    public Net load_onnx_model() throws IOException {
        OpenCVLoader.initDebug();
        return Dnn.readNetFromONNX(Utils.assetFilePath(context, "resnet18_classifier.onnx"));
    }

    public String classify_onnx(String img_path, Net model, List<String> classes) throws IOException {
        // load and preprocess image
        Mat img = Imgcodecs.imread(Utils.assetFilePath(context, img_path), Imgcodecs.IMREAD_COLOR);
        Imgproc.resize(img, img, new Size(128, 128));

        // forward to net
        Log.i("[+++] Img Shape: ", img.size().toString());  // FIXME: dimensions are wrong (128x128 instead of BSx3x128x128)
        model.setInput(img);
        Mat out = model.forward();

        // get argmax
        Core.MinMaxLocResult mm = Core.minMaxLoc(out);
        int maxValIdx = (int)mm.maxLoc.x;

        return classes.get(maxValIdx);
    }

    public Module load_model() throws IOException, URISyntaxException {
        return LiteModuleLoader.load(Utils.assetFilePath(context, "resnet18_classifier.ptl"));
    }

    public String classify(Bitmap img, Module module, List<String> classes) throws IOException {
        // prep image for net (128x128 BGR)
        img = Utils.getResizedBitmap(img, 128, 128);
        Bitmap in_img = Utils.toBGR(img);

        try{
            // FIXME: the problem here is that Bitmap can only be RGB
            //in_img = Bitmap.createBitmap(new_img.cols(), new_img.rows(), Bitmap.Config.)
        }
        catch(CvException e){
            Log.d("Exception", e.getMessage());
        }

        // forward image to net
        Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(in_img, TensorImageUtils.TORCHVISION_NORM_MEAN_RGB, TensorImageUtils.TORCHVISION_NORM_STD_RGB);
        /*
        long[] shape = inputTensor.shape();
        for (long s : shape) {
            Log.i("[+++++] Input Shape: ", Long.toString(s));
        }
        */
        Tensor outputTensor = module.forward(IValue.from(inputTensor)).toTensor();
        float[] scores = outputTensor.getDataAsFloatArray();

        // get argmax
        float maxScore = -Float.MAX_VALUE;
        int maxScoreIdx = -1;
        for (int i = 0; i < scores.length; i++) {
            Log.i("[+]", Float.toString(scores[i]));
            if (scores[i] > maxScore) {
                maxScoreIdx = i;
            }
        }

        return classes.get(maxScoreIdx);
    }
}
