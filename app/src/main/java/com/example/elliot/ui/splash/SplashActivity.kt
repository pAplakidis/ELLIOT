package com.example.elliot.ui.splash

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import com.example.elliot.MainActivity
import com.example.elliot.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val title = findViewById<TextView>(R.id.text_view_id)
        ObjectAnimator.ofFloat(title, "translationX", 100f).apply {
            duration = 2000
            start()
        }

        val splashTimeout = 2050
        val homeIntent = Intent(this@SplashActivity, MainActivity::class.java)

        Handler().postDelayed({
            startActivity(homeIntent)
            finish()
        }, splashTimeout.toLong())
    }
}