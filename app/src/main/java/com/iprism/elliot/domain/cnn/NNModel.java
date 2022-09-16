package com.iprism.elliot.domain.cnn;

import android.content.Context;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

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
}