package com.holiday.diary.repository

import com.holiday.diary.dao.DiaryDao
import com.holiday.diary.entities.Diarys

class DiaryRepository(private val diaryDao: DiaryDao) {

    fun readDateData(year : Int, month : Int, day : Int): List<Diarys> {
        return diaryDao.readDateData(year, month, day)
    }
}