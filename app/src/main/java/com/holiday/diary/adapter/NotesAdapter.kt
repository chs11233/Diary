package com.holiday.diary.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.holiday.diary.entities.Notes
import com.holiday.diary.databinding.ItemNotesBinding
import kotlin.collections.ArrayList

class NotesAdapter() :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    var listener: OnItemClickListener? = null
    var arrList = ArrayList<Notes>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = ItemNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: NotesViewHolder, position: Int) {
        viewHolder.binding.tvNote.text = arrList[position].noteText
        viewHolder.binding.tvDateTime.text = arrList[position].dateTime

        if (arrList[position].color != null) {
            viewHolder.binding.cardView.setCardBackgroundColor(Color.parseColor(arrList[position].color))
        } else {
            viewHolder.binding.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
        }
        viewHolder.binding.cardView.setOnClickListener {
            listener!!.onClicked(arrList[position].id!!)
        }
    }

    override fun getItemCount(): Int {
        return arrList.size
    }

    fun setData(arrNotesList: List<Notes>) {
        arrList = arrNotesList as ArrayList<Notes>
    }

    fun setOnClickListener(listener1: OnItemClickListener) {
        listener = listener1
    }

    class NotesViewHolder(val binding: ItemNotesBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    interface OnItemClickListener {
        fun onClicked(noteId: Int)
    }
}