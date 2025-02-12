package com.kleecollage.geminichatbot.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalView() {

    var showModal by remember {
        mutableStateOf(false)
    }

    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Button(onClick = { showModal = true }) {
            Text(text = "Show Modal")
        }
    }

    if (showModal) {
        ModalBottomSheet(onDismissRequest = { showModal = false }) {
            Text(text = "Hi i am the Modal Sheet")
        }
    }


}