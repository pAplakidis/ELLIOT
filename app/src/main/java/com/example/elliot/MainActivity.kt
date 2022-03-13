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

        val nnModel = NNModel(this)
        val classes = nnModel.load_classes()
        val model = nnModel.load_model()
        val img = nnModel.load_img("images/banaan-large.jpg")

        binding.button.setOnClickListener {
            binding.imageView.setImageBitmap(img)
        }
    }
}