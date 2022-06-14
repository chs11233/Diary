package com.holiday.diary.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.holiday.diary.dao.DiaryDao
import com.holiday.diary.entities.Diarys

@Database(entities = [Diarys::class], version = 1, exportSchema = false)
abstract class DiarysDatabase : RoomDatabase() {

    abstract fun diaryDao(): DiaryDao

    companion object {
        var diarysDatabase: DiarysDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): DiarysDatabase {
            if (diarysDatabase == null) {
                diarysDatabase = Room.databaseBuilder(
                    context, DiarysDatabase::class.java, "diarys.db"
                ).build()
            }
            return diarysDatabase!!
        }
    }
}