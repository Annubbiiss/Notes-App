package com.example.notes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.notes.model.Note
import com.example.notes.model.data.NotesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(private val db :NotesDao) :ViewModel() {

    val note : LiveData<List<Note>> = db.getAllNotes()

    fun deleteNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            db.deleteNote(note)
        }
    }

    fun updateNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            db.updateNote(note)
        }
    }

    suspend fun getNote(id:Int):Note?{
        return db.getNoteById(id)
    }

    fun createNote(title:String, body:String){
        viewModelScope.launch(Dispatchers.IO) {
            db.insertNote(Note(title = title, note = body))
        }
    }


}

class NoteViewModelFactory(private val db : NotesDao) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteViewModel(db) as T
    }
}
