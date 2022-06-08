package com.holiday.diary.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.holiday.diary.databinding.ItemDiarysBinding
import com.holiday.diary.entities.Diarys
import kotlin.collections.ArrayList

class DiarysAdapter() :
    RecyclerView.Adapter<DiarysAdapter.DiarysViewHolder>() {
    var listener: OnItemClickListener? = null
    var arrList = ArrayList<Diarys>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiarysViewHolder {
        val view = ItemDiarysBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiarysViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: DiarysViewHolder, position: Int) {
        viewHolder.binding.dvTitle.text = arrList[position].title
        viewHolder.binding.dvDesc.text = arrList[position].diaryText
        viewHolder.binding.dvDateTime.text = arrList[position].dateTime

        if (arrList[position].imgPath != null){
            viewHolder.itemView.imgNote.setImageBitmap(BitmapFactory.decodeFile(arrList[position].imgPath))
            holder.itemView.imgNote.visibility = View.VISIBLE
        }else{
            holder.itemView.imgNote.visibility = View.GONE
        }

        viewHolder.binding.cardView.setOnClickListener {
            listener!!.onClicked(arrList[position].id!!)
        }
    }

    override fun getItemCount(): Int {
        return arrList.size
    }

    fun setData(arrNotesList: List<Diarys>) {
        arrList = arrNotesList as ArrayList<Diarys>
    }

    fun setOnClickListener(listener2: OnItemClickListener) {
        listener = listener2
    }

    class DiarysViewHolder(val binding: ItemDiarysBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    interface OnItemClickListener {
        fun onClicked(diaryId: Int)
    }

}