package com.holiday.diary.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Diarys")
class Diarys: Serializable {

    @PrimaryKey(autoGenerate = true)
    var id:Int? = null

    @ColumnInfo(name = "title")
    var title:String? = null

    @ColumnInfo(name = "date_time")
    var dateTime:String? = null

    @ColumnInfo(name = "diary_text")
    var diaryText:String? = null

    @ColumnInfo(name = "img_path")
    var imgPath:String? = null

    override fun toString(): String {
        return "$title : $dateTime"
    }
}