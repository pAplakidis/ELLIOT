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
    }

    public List<String> load_classes(String path){
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
}