package com.example.notes

import android.app.Application
import androidx.room.Room
import com.example.notes.model.data.NotesDao
import com.example.notes.model.data.NotesDatabase
import com.example.notes.model.Constants

class NotesApp:Application() {
    private var db : NotesDatabase? = null

    init {
        instance = this
    }

    private fun getDatabase() : NotesDatabase{
        return if (db != null){
            db!!
        } else {
            db = Room.databaseBuilder(
                instance!!.applicationContext,
                NotesDatabase::class.java, Constants.DATABASE_NAME
            ).fallbackToDestructiveMigration()// remove in prod
                .build()
            db!!
        }
    }

    companion object{
        private var instance : NotesApp? = null

        fun getDao() : NotesDao {
            return instance!!.getDatabase().NotesDao()

        }
    }
}