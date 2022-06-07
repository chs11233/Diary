package com.holiday.diary

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.holiday.diary.adapter.NotesAdapter
import com.holiday.diary.database.NotesDatabase
import com.holiday.diary.databinding.FragmentCalBinding
import com.holiday.diary.entities.Notes
import kotlinx.coroutines.launch

class CalFragment : BaseFragment() {
    private var mBinding: FragmentCalBinding? = null
    private val binding get() = mBinding!!

    var arrNotes = ArrayList<Notes>()
    var notesAdapter: NotesAdapter = NotesAdapter()

    private var year : Int = 0
    private var month : Int = 0
    private var day : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = FragmentCalBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CalFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendarRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        binding.calendarRecyclerView.adapter = notesAdapter

        binding.calendarView.setOnDateChangeListener { _, year, month, day ->
            this.year = year
            this.month = month+1
            this.day = day

            binding.calendarDateText.text = "${this.year}/${this.month}/${this.day}"

            launch {
                context?.let {
                    var notes = NotesDatabase.getDatabase(it).noteDao().readDateData(dateTime = day)
                    notesAdapter!!.setData(notes)
                    arrNotes = notes as ArrayList<Notes>
                }
            }
        }
    }
}