package com.example.notes.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notes.R
import com.example.notes.model.Constants
import com.example.notes.model.Note
import com.example.notes.ui.theme.NotesTheme
import com.example.notes.ui.uicomponent.DefaultAppBar
import com.example.notes.viewmodel.NoteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NoteEditScreen (noteId : Int, navController: NavController, viewModel: NoteViewModel){
    val scope = rememberCoroutineScope()
    val note = remember { mutableStateOf(Constants.noteDetailPlaceHolder) }
    val currentNote = remember { mutableStateOf(note.value.note) }
    val currentTitle = remember { mutableStateOf(note.value.title) }
    val saveButtonState = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true ){
        scope.launch(Dispatchers.IO) {
            //elvis
            note.value = viewModel.getNote(noteId)?:Constants.noteDetailPlaceHolder
            currentNote.value = note.value.note
            currentTitle.value = note.value.title

        }
    }

    NotesTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
            , color = MaterialTheme.colorScheme.primary
        ) {
            Scaffold(
                topBar = {
                    DefaultAppBar(
                        title = "Edit note",
                        icon = {
                            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.save_note),
                                contentDescription = "",
                                tint = Color.Black
                            ) },
                        onIconClick = {
                                      viewModel.updateNote(
                                          Note(
                                              id = note.value.id,
                                              note = currentNote.value,
                                              title = currentNote.value
                                          )
                                      )
                            navController.popBackStack()


                        },
                        iconState = saveButtonState
                    )
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                        .padding(top = 50.dp)
                )
                {

                    TextField(
                        value = currentTitle.value,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = Color.Black,
                            focusedLabelColor = Color.Black,
                        ),
                        onValueChange = { value ->
                            currentTitle.value = value
                            if (currentTitle.value != note.value.title) {
                                saveButtonState.value = true
                            } else if (currentNote.value == note.value.note &&
                                currentTitle.value == note.value.title
                            ) {
                                saveButtonState.value = false
                            }
                        },
                        label = { Text(text = "Title") }
                    )

                    TextField(
                        value = currentNote.value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.5f)
                        ,
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = Color.Black,
                            focusedLabelColor = Color.Black,
                        ),
                        onValueChange = { value ->
                            currentNote.value = value
                            if (currentNote.value != note.value.note) {
                                saveButtonState.value = true
                            } else if (currentNote.value == note.value.note &&
                                currentTitle.value == note.value.title
                            ) {
                                saveButtonState.value = false
                            }
                        },
                        label = { Text(text = "Body") }
                    )

                }

            }

        }
    }

}