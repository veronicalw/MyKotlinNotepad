package com.example.mykotlinnotepad

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowManager

class SplashScreenActivity : AppCompatActivity() {
    val screenTimeOut = 4000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        Handler().postDelayed(object: Runnable {
            public override fun run() {
                val Home = Intent(this@SplashScreenActivity, MainActivity::class.java)
                startActivity(Home)
                finish()
            }
        }, screenTimeOut.toLong())
    }
}