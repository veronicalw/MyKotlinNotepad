package com.example.mykotlinnotepad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import com.google.firebase.firestore.DocumentReference

class DetailNotesActivity : AppCompatActivity() {
    private lateinit var titleData: TextView
    private lateinit var contentData: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_notes)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        titleData = findViewById(R.id.titleNotesDetail)
        contentData = findViewById(R.id.noteDetailsContent)

        titleData.setText(intent.getStringExtra("titles"))
        contentData.setText(intent.getStringExtra("contents"))

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.edit_delete_share_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.nav_edit -> {
                val titless = intent.getStringExtra("titles")
                val contentss = intent.getStringExtra("contents")
                val idss = intent.getStringExtra("id")
                val intent = Intent(this, EditNotesActivity::class.java)
                intent.putExtra("titles", titless)
                intent.putExtra("contents", contentss)
                intent.putExtra("id",idss)
                Toast.makeText(this,"Titles : " + titless
                    + "Contents : " + contentss
                    + "ID : " + idss, Toast.LENGTH_LONG).show()
                startActivity(intent)
                return true
            }
            R.id.nav_delete -> {
                return true
            }
            R.id.nav_share -> {
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
