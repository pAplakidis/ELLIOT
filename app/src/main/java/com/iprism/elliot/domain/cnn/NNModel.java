package com.iprism.elliot.domain.cnn;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.iprism.elliot.util.ModelUtils;

import java.io.IOException;

public class NNModel {
    private final Context context;

    public NNModel(Context context) {
        this.context = context;
    }

    public PyObject init() {
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(context));
        }

        Python py = Python.getInstance();

        return py.getModule("model");
    }

    public Bitmap loadImg(String path) throws IOException {
        return ModelUtils.getResizedBitmap(BitmapFactory.decodeStream(context.getAssets().open(path)), 1080, 1080);
    }
}
