package com.example.notes.ui.uicomponent

import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultAppBar(
    title: String,
    onIconClick : (() -> Unit)?,
    icon : @Composable() (() -> Unit)?,
    iconState: MutableState<Boolean>
){
    TopAppBar(
        title = {Text(text = title)},
        actions = { IconButton(
            onClick = { onIconClick?.invoke() },
            content = {
                if (iconState.value){
                    icon?.invoke()
                }
            }

        )

                  },
        modifier = Modifier
            .background(color =  MaterialTheme.colorScheme.onPrimary)

    )

}


