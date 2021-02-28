package com.example.mykotlinnotepad

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var edtName : EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword : EditText
    private lateinit var edtConfirmPassword : EditText
    private lateinit var btnRegister : Button
    private lateinit var txtLinkLogin : TextView
    private lateinit var progressBarRegister : ProgressBar
    lateinit var fireAuth : FirebaseAuth
    lateinit var user : FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Initialize ID of Objects
        edtName = findViewById(R.id.edtName)
        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        txtLinkLogin = findViewById(R.id.txtLinkLogin)
        progressBarRegister = findViewById(R.id.progressBarRegister)

        //get Firestore instance
        fireAuth = FirebaseAuth.getInstance()

        //Register button link
        btnRegister.setOnClickListener {
            var name = edtName.text.toString()
            var email = edtEmail.text.toString()
            var pass = edtPassword.text.toString()
            var confirmPass = edtConfirmPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()){
                Toast.makeText(this, "Fields can not be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (!pass.equals(confirmPass)){
                edtConfirmPassword?.setError("Password is not match or Password must contain at least 6 characters")
            }

                val authCredential: AuthCredential = EmailAuthProvider.getCredential(email, pass)
                progressBarRegister.visibility = View.VISIBLE

                fireAuth.currentUser?.linkWithCredential(authCredential)?.addOnSuccessListener {
                    Toast.makeText(this, "Successfully Registered and Synced", Toast.LENGTH_LONG)
                        .show()
                    val intents = Intent(this, MainActivity::class.java)
                    startActivity(intents)
                    finish()
                    user = fireAuth.currentUser!!
                    //Get User Name to Display in Navigation Header
                    val userRequest = UserProfileChangeRequest.Builder().apply {
                        displayName = name
                    }.build()
                    user.updateProfile(userRequest)

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }?.addOnFailureListener {
                    Toast.makeText(this, "Failed to connect, Please Try Again ", Toast.LENGTH_LONG)
                        .show()
                    progressBarRegister.visibility = View.GONE
                }
        }
        // Login link
        txtLinkLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}
