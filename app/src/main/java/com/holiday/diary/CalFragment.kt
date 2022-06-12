package com.holiday.diary

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.holiday.diary.adapter.DiarysAdapter
import com.holiday.diary.databinding.FragmentCalBinding
import com.holiday.diary.entities.Diarys
import com.holiday.diary.viewmodel.DiaryViewModel
import androidx.fragment.app.viewModels

class CalFragment : BaseFragment() {
    private var mBinding: FragmentCalBinding? = null
    private val binding get() = mBinding!!

    private val diaryViewModel: DiaryViewModel by viewModels()
    var diarysAdapter: DiarysAdapter = DiarysAdapter()
    var arrDiarys = ArrayList<Diarys>()

    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

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

        binding.calendarRecyclerView.setHasFixedSize(true)
        binding.calendarRecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.calendarRecyclerView.adapter = diarysAdapter

        binding.calendarView.setOnDateChangeListener { _, year, month, day ->
            this.year = year
            this.month = month + 1
            this.day = day

            binding.calendarDateText.text = "${this.year}/${this.month}/${this.day}"

            diaryViewModel.readDateData(this.year,this.month,this.day)
        }
    }
}