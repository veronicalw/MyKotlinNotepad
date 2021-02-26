package com.example.mykotlinnotepad

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class SplashScreenActivity : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    val screenTimeOut = 4000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        firebaseAuth = FirebaseAuth.getInstance()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        Handler().postDelayed(object : Runnable {
            public override fun run() {
                if (firebaseAuth.currentUser != null) {
                    val Home = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    startActivity(Home)
                    finish()
                } else {
                    firebaseAuth.signInAnonymously().addOnSuccessListener(object :
                        OnSuccessListener<AuthResult> {
                        override fun onSuccess(p0: AuthResult?) {
                            Toast.makeText(
                                this@SplashScreenActivity,
                                "Logged in Anonymously",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                            finish()
                        }
                    })
                        .addOnFailureListener{
                            Toast.makeText(
                                this@SplashScreenActivity,
                                "Error in : " + it.localizedMessage.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                }
            }
        }, screenTimeOut.toLong())
    }
}