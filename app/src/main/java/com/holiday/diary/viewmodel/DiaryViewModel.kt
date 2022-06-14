package com.holiday.diary.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.holiday.diary.entities.Diarys
import com.holiday.diary.repository.DiaryRepository

class DiaryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DiaryRepository(application)
    private val _date = MutableLiveData<Int>()
    val date: LiveData<Int>
        get() = _date

    fun getAllByDate(year: Int, month: Int, day: Int): LiveData<List<Diarys>?> {
        return repository.getAllByDate(year, month, day)
    }

    fun updateDate(year: Int, month: Int, day: Int) {
        _date.value = year
        _date.value = month
        _date.value = day
    }

}