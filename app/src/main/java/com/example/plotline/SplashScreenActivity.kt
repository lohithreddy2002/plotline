package com.example.plotline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.plotline.home.HomeActivity
import org.opencv.android.OpenCVLoader

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        OpenCVLoader.initDebug()
        startActivity(Intent(this,HomeActivity::class.java))
        finish()
    }
}