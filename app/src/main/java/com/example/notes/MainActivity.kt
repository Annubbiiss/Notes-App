package com.example.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notes.model.Constants
import com.example.notes.ui.screens.CreateNoteScreen
import com.example.notes.ui.screens.NoteEditScreen
import com.example.notes.ui.screens.NoteListScreen
import com.example.notes.viewmodel.NoteViewModel
import com.example.notes.viewmodel.NoteViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = NoteViewModelFactory(NotesApp.getDao()).create(NoteViewModel::class.java)

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = Constants.NAVIGATION_NOTES_LIST){
                // Notes List
                composable(Constants.NAVIGATION_NOTES_LIST) { NoteListScreen(navController, viewModel) }


                composable(Constants.NAVIGATION_NOTE_EDIT,
                    arguments = listOf(navArgument(Constants.NAVIGATION_NOTE_ID_Argument){
                    type = NavType.IntType
                })
                ) {navBackStackEntry->
                    navBackStackEntry.arguments?.getInt(Constants.NAVIGATION_NOTE_ID_Argument)?.let {
                        NoteEditScreen(noteId = it ,navController, viewModel)
                    }

                }

                composable(Constants.NAVIGATION_NOTES_CREATE) { CreateNoteScreen(navController, viewModel) }



            }

        }
    }
}
