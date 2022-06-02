package com.holiday.diary

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.holiday.diary.database.NotesDatabase
import com.holiday.diary.databinding.FragmentCreateNoteBinding
import com.holiday.diary.entities.Notes
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CreateNoteFragment : BaseFragment() {

    private var mBinding: FragmentCreateNoteBinding? = null
    private val binding get() = mBinding!!

    var currentDate: String? = null
    private var noteId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        noteId = requireArguments().getInt("noteId", -1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = FragmentCreateNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreateNoteFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (noteId != -1) {
            launch {
                context?.let {
                    var notes = NotesDatabase.getDatabase(it).noteDao().getSpecificNote(noteId)
                    binding.colorView.setBackgroundColor(Color.parseColor(notes.color))
                    binding.etNote.setText(notes.noteText)
                }
            }
        }

        val date = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        currentDate = date.format(Date())

        binding.tvDateTime.text = currentDate

        binding.imgDone.setOnClickListener {
            if (noteId != -1) {
                updateNote()
            } else {
                saveNote()
            }
        }

        binding.imgBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun saveNote() {
        if (binding.etNote.text.isNullOrEmpty()) {
            Toast.makeText(context, "내용을 입력해주세요", Toast.LENGTH_SHORT).show()
        } else {

            launch {
                val notes = Notes()
                notes.noteText = binding.etNote.text.toString()
                notes.dateTime = currentDate

                context?.let {
                    NotesDatabase.getDatabase(it).noteDao().insertNotes(notes)
                    binding.etNote.setText("")
                    requireActivity().supportFragmentManager.popBackStack()
                }

            }
        }
    }

    private fun updateNote(){
        launch {
            context?.let {
                var notes = NotesDatabase.getDatabase(it).noteDao().getSpecificNote(noteId)
                notes.noteText = binding.etNote.text.toString()
                notes.dateTime = currentDate

                NotesDatabase.getDatabase(it).noteDao().updateNote(notes)
                binding.etNote.setText("")
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun deleteNote(){

        launch {
            context?.let {
                NotesDatabase.getDatabase(it).noteDao().deleteSpecificNote(noteId)
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

}