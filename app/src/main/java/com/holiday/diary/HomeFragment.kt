package com.holiday.diary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.holiday.diary.adapter.DiarysAdapter
import com.holiday.diary.database.DiarysDatabase
import com.holiday.diary.databinding.FragmentHomeBinding
import com.holiday.diary.entities.Diarys
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : BaseFragment() {
    private var mBinding: FragmentHomeBinding? = null
    private val binding get() = mBinding!!

    var arrDiarys = ArrayList<Diarys>()
    var diarysAdapter: DiarysAdapter = DiarysAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        launch {
            context?.let {
                var diarys = DiarysDatabase.getDatabase(it).diaryDao().getAllDiarys()
                diarysAdapter!!.setData(diarys)
                arrDiarys = diarys as ArrayList<Diarys>
                binding.recyclerView.adapter = diarysAdapter
            }
        }

        diarysAdapter!!.setOnClickListener(onClicked)

        binding.fabCreate.setOnClickListener {
            replaceFragment(CreateDiaryFragment.newInstance(), false)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                var tempArr = ArrayList<Diarys>()

                for (arr in arrDiarys) {
                    if (arr.title!!.lowercase(Locale.getDefault()).contains(p0.toString())) {
                        tempArr.add(arr)
                    }
                }

                diarysAdapter.setData(tempArr)
                diarysAdapter.notifyDataSetChanged()
                return true
            }
        })
    }

    private val onClicked = object : DiarysAdapter.OnItemClickListener {
        override fun onClicked(diarysId: Int) {

            var fragment: Fragment
            var bundle = Bundle()
            bundle.putInt("diaryId", diarysId)
            fragment = CreateDiaryFragment.newInstance()
            fragment.arguments = bundle

            replaceFragment(fragment, false)
        }
    }

    fun replaceFragment(fragment: Fragment, istransition: Boolean) {
        val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()

        if (istransition) {
            fragmentTransition.setCustomAnimations(android.R.anim.slide_out_right,
                android.R.anim.slide_in_left)
        }
        fragmentTransition.replace(R.id.frame_layout, fragment)
            .addToBackStack(fragment.javaClass.simpleName).commit()
    }
}