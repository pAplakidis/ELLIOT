package com.iprism.elliot

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.iprism.elliot.ui.camera.CameraActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startFrontendApp()

        if (intent.extras != null) {
            sharedPref.edit().putString("username", intent.extras?.getString("USERNAME")).apply()
            sharedPref.edit().putString("token", intent.extras?.getString("TOKEN")).apply()
            sharedPref.edit().putString("patientname", intent.extras?.getString("PATIENTNAME"))
                .apply()
        }
    }

    override fun onRestart() {
        super.onRestart()
        startFrontendApp()
    }

    override fun onResume() {
        super.onResume()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)

        val cameraButton = findViewById<FloatingActionButton>(R.id.floating_action_button)
        cameraButton.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
    }

    private fun startFrontendApp() {
        if (intent.action != null && intent.extras == null)
            try {
                startActivity(packageManager.getLaunchIntentForPackage("eu.edgeneering.pt002_gewi"))
            } catch (e: NullPointerException) {
                Toast.makeText(this, getString(R.string.fa_not_installed), Toast.LENGTH_LONG).show()
                finishAndRemoveTask()
            }
    }

}