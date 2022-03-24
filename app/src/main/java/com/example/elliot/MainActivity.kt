package com.example.elliot;

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.elliot.databinding.ActivityMainBinding;
import com.example.elliot.NNModel;

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // model stuff
        val img_path = "val_000011.jpg";
        val model_path = Utils.assetFilePath(this, "resnet18_classifier.pth")
        val classes_path = Utils.assetFilePath(this, "classes.json")

        val nnModel = NNModel(this)
        val pyobj = nnModel.init()

        //val classes = nnModel.load_classes()
        //val model = nnModel.load_model()
        //val model = nnModel.load_onnx_model()
        val img = nnModel.load_img(img_path)

        binding.button.setOnClickListener {
            binding.imageView.setImageBitmap(img)
        }

        binding.classifyButton.setOnClickListener{
            //val food = nnModel.classify(img_path, model, classes)
            //val food = nnModel.classify_onnx(img_path, model, classes)
            val food = pyobj.callAttr("classify", Utils.assetFilePath(this, img_path), model_path, classes_path)
            binding.classifyText.setText(food.toString());
        }
    }
}