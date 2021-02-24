package com.example.mykotlinnotepad.models

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mykotlinnotepad.DetailNotesActivity
import com.example.mykotlinnotepad.R
import kotlin.random.Random

class NotesAdapter(val title:List<String>,val content:List<String>) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {
//
    init{
        title
        content
    }

    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): NotesAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item_test, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotesAdapter.ViewHolder, position: Int) {
        holder.txtTitle.text = title.get(position)
        holder.txtContent.text = content.get(position)
//        holder.cvNote.cardBackgroundColor = holder.view.resources.getColor(getRandomColour(),null)
//        holder?.txtTitle.setText(titles.get(position))
//        holder?.txtContent.setText(contents.get(position))
//        holder?.cvNote.setCardBackgroundColor(holder.view.resources.getColor(getRandomColour(),null))
        val context = holder.txtTitle.context
        holder.view.setOnClickListener {
            val intent = Intent(context, DetailNotesActivity::class.java)
            context.startActivity(intent)
        }
    }

    fun getRandomColour(): Int {
        val colours = arrayListOf<Int>()
        colours.addAll(listOf(
                R.color.cornsilk,
                R.color.blanchedalmond,
                R.color.bisque,
                R.color.navajowhite,
                R.color.wheat,
                R.color.burlywood,
                R.color.tan,
                R.color.rosybrown,
                R.color.sandybrown,
                R.color.goldenrod,
                R.color.peru,
                R.color.chocolate,
                R.color.saddlebrown,
                R.color.brown,
        ))

        var randomColour = Random
        val number = randomColour.nextInt(colours.size)
        return colours.get(number)
    }

    override fun getItemCount(): Int {
        return title.size
    }

    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val txtTitle: TextView = itemView.findViewById(R.id.titleNote)
        internal val txtContent: TextView = itemView.findViewById(R.id.txtNoteContent)
        internal val cvNote: CardView = itemView.findViewById(R.id.cvView)
        internal val view: View = itemView
    }
}