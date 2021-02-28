package com.example.mykotlinnotepad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class EditNotesActivity : AppCompatActivity() {
    private lateinit var edtEditTitle: EditText
    private lateinit var edtEditContent: EditText
    private lateinit var progressBar: ProgressBar
    lateinit var firestore: FirebaseFirestore
    lateinit var firebaseUser: FirebaseUser
    private var TAG : String ="TAGDELETE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_notes)
        setSupportActionBar(findViewById(R.id.toolbarsEdit))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        edtEditTitle = findViewById(R.id.editTitle)
        edtEditContent = findViewById(R.id.editContent)
        progressBar = findViewById(R.id.progressBarEdit)

        edtEditTitle.setText(intent.getStringExtra("titles").toString())
        edtEditContent.setText(intent.getStringExtra("contents").toString())

        firestore = FirebaseFirestore.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser!!

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.add_act_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.nav_save -> {
                val valueTitle = edtEditTitle.text.toString()
                val valueContent = edtEditContent.text.toString()
                if (valueTitle.isEmpty() && valueContent.isEmpty()) {
                    Toast.makeText(this, "your note is empty!", Toast.LENGTH_LONG).show()
                    return false
                }
                progressBar.visibility = View.VISIBLE
                val documentReference: DocumentReference =
                    firestore.collection("hinotes").document(firebaseUser.uid).collection("hiNotesUser").document(intent.getStringExtra("id").toString())
                var note: HashMap<String, Any> = HashMap<String, Any>()
                note.put("titles", valueTitle)
                note.put("contents", valueContent)
                documentReference.update(note).addOnSuccessListener {
                    Toast.makeText(this,"Successfully Saved!", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this,"Error save!", Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.VISIBLE
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}