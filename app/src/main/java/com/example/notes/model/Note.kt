package com.example.notes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = Constants.TABLE_NAME, indices = [Index(value = ["id"], unique = true)])
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "body")
    val note :String,
    @ColumnInfo(name = "title")
    val title:String,
    @ColumnInfo(name = "dateUpdated")
    val date : String = getDate()
)

fun getDate():String{
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss"))
}

fun Note.getDay(): String? {
    if (this.date.isEmpty()) return ""
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss")
    return LocalDateTime.parse(this.date,formatter).toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}

val placeHolderList = listOf(Note(title = "No thing to show", note = "No thing to show", id = 0))

fun List<Note>?.orPlaceHolderList(): List<Note> {
    return if (this != null && this.isNotEmpty()){
        this
    }else placeHolderList
}