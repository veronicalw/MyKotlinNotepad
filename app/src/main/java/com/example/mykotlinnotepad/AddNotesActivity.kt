package com.example.mykotlinnotepad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.Toolbar
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.HashMap

class AddNotesActivity : AppCompatActivity() {
    private lateinit var edtTitle: EditText
    private lateinit var edtContent: EditText
    lateinit var firestore: FirebaseFirestore
    private lateinit var progressBar: ProgressBar
    lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)
        setSupportActionBar(findViewById(R.id.toolbars))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        edtTitle = findViewById(R.id.edtNoteTitle)
        edtContent = findViewById(R.id.edtNoteContent)
        progressBar = findViewById(R.id.progressBar)
        firestore = FirebaseFirestore.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.add_act_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.nav_save -> {
                val valueTitle = edtTitle.text.toString()
                val valueContent = edtContent.text.toString()
                if (valueTitle.isEmpty() && valueContent.isEmpty()) {
                    Toast.makeText(this, "your note is empty!", Toast.LENGTH_LONG).show()
                    return false
                }
                progressBar.visibility = View.VISIBLE
                val documentReference: DocumentReference =
                    firestore.collection("hinotes").document(firebaseUser.getUid()).collection("hiNotesUser").document()
                var note: HashMap<String, Any> = HashMap<String, Any>()
                note.put("titles", valueTitle)
                note.put("contents", valueContent)
                documentReference.set(note).addOnSuccessListener {
                    Toast.makeText(this,"Successfully Saved!", Toast.LENGTH_LONG).show()
                    onBackPressed()
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