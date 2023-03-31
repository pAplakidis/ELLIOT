package com.iprism.elliot

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.iprism.elliot.data.repository.ApiService
import com.iprism.elliot.domain.model.RequestModel
import com.iprism.elliot.domain.model.ResponseModel
import com.iprism.elliot.ui.camera.CameraActivity
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
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

        sendData()

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

    private fun sendData() {
        val username = sharedPref.getString("username","")
        val token = sharedPref.getString("token","")
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR).toString()
        val month = (calendar.get(Calendar.MONTH) + 1).toString()
        val day = calendar.get(Calendar.DAY_OF_MONTH).toString()

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val date = "$day/$month/$year"
        val time = "$hour:$minute"

        val client = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://195.251.108.189")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        // Create an instance of the interface:

        val apiService = retrofit.create(ApiService::class.java)

// Create the request body:

        val body = RequestModel(token.toString(),username.toString(),date,time)

// Make the request:

        apiService.postRequest(body).enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                Log.d("Success message",response.toString())
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Log.d("Error message",t.toString())
            }
        })

    }

}