package com.example.mykotlinnotepad

import android.content.DialogInterface
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var edtSignInEmail : EditText
    private lateinit var edtSignInPassword : EditText
    private lateinit var btnSignIn : Button
    private lateinit var txtForgotPassword : TextView
    private lateinit var txtCreateAnAccount: TextView
    private lateinit var progressBarLogin : ProgressBar
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseStore: FirebaseFirestore
    lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        edtSignInEmail = findViewById(R.id.edtSignInEmail)
        edtSignInPassword = findViewById(R.id.edtSignInPassword)
        btnSignIn = findViewById(R.id.btnSignIn)
        txtForgotPassword = findViewById(R.id.txtForgotPassword)
        txtCreateAnAccount = findViewById(R.id.txtCreateAccount)
        progressBarLogin = findViewById(R.id.progressBarLogin)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseStore = FirebaseFirestore.getInstance()
        firebaseUser = firebaseAuth.currentUser!!

        displayAlert()

        btnSignIn.setOnClickListener {
            var email = edtSignInEmail.text.toString()
            var password = edtSignInPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Email and Password can not be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            progressBarLogin.visibility = View.VISIBLE

            if (firebaseAuth.currentUser?.isAnonymous == true){
                var user: FirebaseUser
                user = firebaseAuth.currentUser!!
                firebaseStore.collection("hinotes").document(user.uid).delete().addOnSuccessListener {
                    Toast.makeText(this,"All temporary notes are deleted", Toast.LENGTH_LONG).show()
                }
                user.delete().addOnSuccessListener {
                    Toast.makeText(this,"Anonymous or temporary user deleted", Toast.LENGTH_LONG).show()

                }
            }

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener{
                Toast.makeText(this, "Successfully Login! ", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this,"Failed Login! ", Toast.LENGTH_LONG).show()
                progressBarLogin.visibility = View.GONE
            }
        }

        txtForgotPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPassActivity::class.java)
            startActivity(intent)
        }

        txtCreateAnAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun displayAlert() {
        val alertDialog = AlertDialog.Builder(this).setTitle("Are you sure?")
            .setMessage("Login to existing account will delete you temporary notes, create new account to save them")
            .setPositiveButton("Save and Sync Note", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    firebaseUser?.delete()?.addOnSuccessListener{
                        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            })
            .setNegativeButton("Continue", object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                }
            })
        alertDialog.show()
    }
}