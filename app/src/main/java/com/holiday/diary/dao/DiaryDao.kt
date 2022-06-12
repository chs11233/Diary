package com.holiday.diary.dao

import androidx.constraintlayout.helper.widget.Flow
import androidx.room.*
import com.holiday.diary.entities.Diarys

@Dao
interface DiaryDao {

    @Query("SELECT * FROM diarys ORDER BY id DESC")
    suspend fun getAllDiarys() : List<Diarys>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiarys(diary:Diarys)

    @Update
    suspend fun updateDiary(diary:Diarys)

    @Delete
    suspend fun deleteDiarys(diary:Diarys)

    @Query("SELECT * FROM diarys WHERE id =:id")
    suspend fun getSpecificDiary(id:Int) : Diarys

    @Query("DELETE FROM diarys WHERE id =:id")
    suspend fun deleteSpecificDiary(id:Int)

    @Query("SELECT * FROM diarys WHERE year = :year AND month = :month AND day = :day ORDER BY id DESC")
    fun readDateData(year : Int, month : Int, day : Int) : List<Diarys>
}