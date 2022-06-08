package com.holiday.diary.dao

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

}