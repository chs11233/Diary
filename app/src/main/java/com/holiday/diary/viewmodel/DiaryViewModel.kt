package com.holiday.diary.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.holiday.diary.database.DiarysDatabase
import com.holiday.diary.entities.Diarys
import com.holiday.diary.repository.DiaryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiaryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : DiaryRepository

    private var _currentData = MutableLiveData<List<Diarys>>()
    val currentData : LiveData<List<Diarys>>
        get() = _currentData

    init{
        val diaryDao = DiarysDatabase.getDatabase(application)!!.diaryDao()
        repository = DiaryRepository(diaryDao)
    }
    fun readDateData(year : Int, month : Int, day : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val tmp =  repository.readDateData(year, month, day)
            _currentData.postValue(tmp)
        }
    }
}