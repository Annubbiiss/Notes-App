package com.example.notes.model.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.notes.model.Note

@Dao
interface NotesDao {
    @Query("SELECT * FROM Notes WHERE notes.id=:id")
    suspend fun getNoteById(id : Int): Note?

    @Query("SELECT * FROM Notes ORDER BY dateUpdated DESC")
    fun getAllNotes() : LiveData<List<Note>>

    @Delete
    fun deleteNote(note: Note):Int

    @Update
    fun updateNote(note: Note):Int

    @Insert
    fun insertNote(note: Note)
}