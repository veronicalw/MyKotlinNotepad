package com.example.mykotlinnotepad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPassActivity : AppCompatActivity() {
    private lateinit var edtForgotPassEmail : EditText
    private lateinit var btnConfirm : Button
    private lateinit var txtCancel : TextView
    private lateinit var txtCreateAccount : TextView

    lateinit var firebaseAuth: FirebaseAuth
    private var TAG : String = "TAG"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)

        edtForgotPassEmail = findViewById(R.id.edtEmail)
        btnConfirm = findViewById(R.id.btnConfirm)
        txtCancel = findViewById(R.id.txtCancel)
        txtCreateAccount = findViewById(R.id.txtCreateAccount)
        firebaseAuth = FirebaseAuth.getInstance()

        btnConfirm.setOnClickListener {
//            Toast.makeText(this,"Email : " + , Toast.LENGTH_SHORT).show()
            firebaseAuth.sendPasswordResetEmail(edtForgotPassEmail.text.toString())
                .addOnCompleteListener {
                        task ->
                    if (task.isSuccessful){
                        Toast.makeText(this,"Please check your inbox, we've sent you an email to change your passwords :D ", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@ForgotPassActivity, LoginActivity::class.java)
                        Log.d(TAG,"Email Sent")
                        startActivity(intent)
                    }
                }
        }

        txtCancel.setOnClickListener {
            val intent = Intent(this@ForgotPassActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        txtCreateAccount.setOnClickListener {
            val intent = Intent(this@ForgotPassActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}