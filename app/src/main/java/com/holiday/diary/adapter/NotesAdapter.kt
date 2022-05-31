package com.holiday.diary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.holiday.diary.entities.Notes
import com.holiday.diary.databinding.ItemNotesBinding

class NotesAdapter() :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
//    var listener:OnItemClickListener? = null
    var arrList = ArrayList<Notes>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = ItemNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        var viewHolder = (holder as NotesViewHolder).binding

        viewHolder.tvNote.text = arrList[position].noteText
        viewHolder.tvDateTime.text = arrList[position].dateTime
    }

    override fun getItemCount(): Int {
        return arrList.size
    }

    fun setData(arrNotesList: List<Notes>){
        arrList = arrNotesList as ArrayList<Notes>
    }

//    fun setOnClickListener(listener1: OnItemClickListener){
//        listener = listener1
//    }

    class NotesViewHolder(val binding: ItemNotesBinding) : RecyclerView.ViewHolder(binding.root){

    }

//    interface OnItemClickListener{
//        fun onClicked(noteId:Int)
//    }
}