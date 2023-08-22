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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notes.R
import com.example.notes.ui.theme.NotesTheme
import com.example.notes.ui.uicomponent.DefaultAppBar
import com.example.notes.viewmodel.NoteViewModel


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateNoteScreen(
    navController: NavController,
    viewModel: NoteViewModel
) {


    val currentNote = remember { mutableStateOf("") }
    val currentTitle = remember { mutableStateOf("") }
    val saveButtonState = remember { mutableStateOf(false) }



    NotesTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primary) {
            Scaffold(

                topBar = {
                    DefaultAppBar(
                        title = "Create Note",
                        icon = {
                            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.save_note),
                                contentDescription = "",
                                tint = Color.Black
                            ) },
                        onIconClick = {
                            viewModel.createNote(
                                currentTitle.value,
                                currentNote.value,
                            )
                            navController.popBackStack()
                        },
                        iconState = saveButtonState
                    )
                },
                content = {

                    Column(
                        Modifier
                            .padding(12.dp)
                            .padding(top = 50.dp)
                            .fillMaxSize()
                    ) {


                        TextField(
                            value = currentTitle.value,
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.textFieldColors(
                                cursorColor = Color.Black,
                                focusedLabelColor = Color.Black,
                            ),
                            onValueChange = { value ->
                                currentTitle.value = value
                                saveButtonState.value = currentTitle.value != "" && currentNote.value != ""
                            },
                            label = { Text(text = "Title") }
                        )
                        TextField(
                            value = currentNote.value,
                            colors = TextFieldDefaults.textFieldColors(
                                cursorColor = Color.Black,
                                focusedLabelColor = Color.Black,
                            ),
                            modifier = Modifier
                                .fillMaxHeight(0.5f)
                                .fillMaxWidth(),
                            onValueChange = { value ->
                                currentNote.value = value
                                saveButtonState.value =  currentNote.value != ""
                            },
                            label = { Text(text = "Body") }
                        )
                    }
                }


            )

        }
    }
}