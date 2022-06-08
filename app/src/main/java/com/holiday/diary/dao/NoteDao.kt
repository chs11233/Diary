package com.holiday.diary.dao

import androidx.room.*
import com.holiday.diary.entities.Notes

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY id DESC")
    suspend fun getAllNotes() : List<Notes>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(note:Notes)

    @Update
    suspend fun updateNote(note:Notes)

    @Delete
    suspend fun deleteNotes(note: Notes)

    @Query("SELECT * FROM notes WHERE id =:id")
    suspend fun getSpecificNote(id:Int) : Notes

    @Query("DELETE FROM notes WHERE id =:id")
    suspend fun deleteSpecificNote(id:Int)

}