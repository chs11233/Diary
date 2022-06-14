package com.holiday.diary.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.holiday.diary.dao.DiaryDao
import com.holiday.diary.database.DiarysDatabase
import com.holiday.diary.entities.Diarys

class DiaryRepository(application: Application) {
    private val diaryDao: DiaryDao

    init {
        var db = DiarysDatabase.getDatabase(application)
        diaryDao = db!!.diaryDao()
    }

    fun getAllByDate(year: Int, month: Int, day: Int): LiveData<List<Diarys>?> {
        return diaryDao.getAllByDate(year, month, day)
    }
}