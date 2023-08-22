package com.example.notes.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notes.R
import com.example.notes.model.Constants
import com.example.notes.model.Note
import com.example.notes.model.getDay
import com.example.notes.model.orPlaceHolderList
import com.example.notes.ui.theme.NotesTheme
import com.example.notes.ui.theme.noteBGBlue
import com.example.notes.ui.theme.noteBGYellow
import com.example.notes.ui.uicomponent.DefaultAppBar
import com.example.notes.viewmodel.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NoteListScreen(navController: NavController, viewModel: NoteViewModel)
{
    val deleteText = remember { mutableStateOf("") }
    val noteQuery = remember { mutableStateOf("") }
    val notesToDelete = remember { mutableStateOf(listOf<Note>()) }
    val openDialog = remember { mutableStateOf(false) }
    val notes = viewModel.note.observeAsState()
    val context = LocalContext.current


    NotesTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primary) {
            Scaffold(
                topBar = {
                    DefaultAppBar(
                        title = "Notes ",

                        onIconClick = {
                            if (notes.value?.isNotEmpty() == true){
                                openDialog.value = true
                                deleteText.value = "Are you sure to delete this"
                                notesToDelete.value = notes.value?: emptyList()

                            }else{
                                Toast.makeText(context,"No notes found",Toast.LENGTH_SHORT).show()
                            }
                        },
                        icon = {
                            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.note_delete),
                                contentDescription = "",
                                tint = Color.Black
                            ) },
                        iconState = remember { mutableStateOf(true) }
                    )
                },
                floatingActionButton = {
                    NotesFab(
                        contentDescription = "add Note",
                        icon = R.drawable.add_note,
                        contentColorFor(backgroundColor = Color.White)
                    ) {
                        navController.navigate(Constants.NAVIGATION_NOTES_CREATE)
                    }

                },
                content = {

                    Column {
                        SearchBar(query = noteQuery)
                        NoteList(
                            notes = notes.value.orPlaceHolderList(),
                            query = noteQuery,
                            openDialog = openDialog,
                            deleteText = deleteText,
                            navController = navController,
                            notesToDelete = notesToDelete
                        )

                    }
                    DeleteDialog(openDialog = openDialog,
                        text = deleteText,
                        notesToDelete = notesToDelete,
                        action = { notesToDelete.value.forEach{
                            viewModel.deleteNote(it)
                        }
                        }
                    )
                }
                )


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(query: MutableState<String>){
    Column(Modifier
        .padding(start = 12.dp, top = 65.dp, end = 12.dp, bottom = 0.dp)
    ) {
        TextField(
            value = query.value,
            placeholder = { Text(text = "Search... ") },
            onValueChange = {query.value = it},
            maxLines = 1,
            modifier = Modifier
                .background(Color.White)
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black
            ),
            trailingIcon = {
                AnimatedVisibility(visible = query.value.isNotEmpty(), enter = fadeIn(), exit = fadeOut()) {
                    IconButton(onClick = { query.value = "" }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.note_clear),
                            contentDescription = "",

                            )
                    }
                }

            }
        )
    }
}

@Composable
fun NoteList(
    notes: List<Note>,
    query: MutableState<String>,
    openDialog: MutableState<Boolean>,
    deleteText: MutableState<String>,
    navController: NavController,
    notesToDelete: MutableState<List<Note>>
)
{
var previousHeader = ""

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(12.dp),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)

    )
    {
        val queryNotes = if (query.value.isEmpty()){
            notes
        }else{
            notes.filter { it.note.contains(query.value) || it.title.contains(query.value) }
        }
        itemsIndexed(queryNotes){index,note->
            if (note.getDay() != previousHeader){
                Column(
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxWidth()
                ) {
                    note.getDay()?.let { Text(text = it, color = Color.Black) }
                }

                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                )
                previousHeader = note.getDay().toString()
            }

            NoteListItem(
                note,
                openDialog,
                deleteText = deleteText ,
                navController,
                notesToDelete = notesToDelete,
                noteBackGround = if (index % 2 == 0) {
                    noteBGYellow
                } else noteBGBlue
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
            )

        }

    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteListItem(
    note: Note,
    openDialog: MutableState<Boolean>,
    deleteText: MutableState<String>,
    navController: NavController,
    noteBackGround: Color,
    notesToDelete: MutableState<List<Note>>
) {

    return Box(modifier = Modifier
        .height(120.dp)
        .padding(6.dp)
        .clip(RoundedCornerShape(12.dp))) {
        Column(
            modifier = Modifier
                .background(noteBackGround)
                .fillMaxWidth()
                .height(120.dp)
                .padding(12.dp)
                .combinedClickable(interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false), // You can also change the color and radius of the ripple
                    onClick = {
                        if (note.id != 0) {
                            navController.navigate(Constants.noteEditNavigation(note.id ?: 0))
                        }
                    },
                    onLongClick = {
                        if (note.id != 0) {
                            openDialog.value = true
                            deleteText.value = "Are you sure to delete this note ?"
                            notesToDelete.value = mutableListOf(note)
                        }
                    }
                )

        ) {
                    Text(
                        text = note.title,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    Text(
                        text = note.note,
                        color = Color.Black,
                        maxLines = 3,
                        modifier = Modifier.padding(12.dp)
                    )
                    Text(
                        text = note.date,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

        }
    }
}

@Composable
fun NotesFab(contentDescription: String, icon: Int, action1: Color, action: () -> Unit) {
    return FloatingActionButton(
        onClick = { action.invoke() },
        Modifier
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Icon(
            ImageVector.vectorResource(id = icon),
            contentDescription = contentDescription,
            tint = Color.Black
        )

    }
}


@Composable
fun DeleteDialog(
    openDialog: MutableState<Boolean>,
    text: MutableState<String>,
    action: () -> Unit,
    notesToDelete: MutableState<List<Note>>
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            confirmButton = {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    onClick = {
                        action.invoke()
                        openDialog.value = false
                        notesToDelete.value = mutableListOf()
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    onClick = {
                        openDialog.value = false
                        notesToDelete.value = mutableListOf()
                    }
                ) {
                    Text("No")
                }
            },
            title = {
                Text(text = "Delete Note")
            },
            text = {
                Column() {
                    Text(text.value)
                }
            },

        )
    }
}