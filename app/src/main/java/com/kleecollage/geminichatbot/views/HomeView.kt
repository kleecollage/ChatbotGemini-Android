package com.kleecollage.geminichatbot.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kleecollage.geminichatbot.components.GlobeMessage
import com.kleecollage.geminichatbot.components.MessageInput
import com.kleecollage.geminichatbot.components.Title
import com.kleecollage.geminichatbot.ui.theme.backColor
import com.kleecollage.geminichatbot.viewModel.GeminiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(viewModel: GeminiViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Title() },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backColor
                ),
                actions = {
                    IconButton(onClick = {viewModel.deleteChat()}) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                    }
                }
            )
        }
    ) { pad ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(pad)
                .background(backColor)
        ) {
            var showModal by remember {
                mutableStateOf(false)
            }
            ChatContent(modifier = Modifier.weight(1f), viewModel)
            Row(verticalAlignment = Alignment.CenterVertically ) {
                IconButton(onClick = { showModal = true }) {
                    Icon(imageVector = Icons.Filled.Camera, contentDescription = "Camera")
                }
                MessageInput {
                    viewModel.sendMessage(it)
                }
            }
            ModalView(viewModel, showModal) {
                viewModel.cleanVars()
                showModal = false
            }
        }
    }
}

@Composable
fun ChatContent(modifier: Modifier, viewModel: GeminiViewModel) {
    LaunchedEffect(Unit) {
        viewModel.loadChat()
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        reverseLayout = true
    ) {
        items(viewModel.messageList.reversed()) {
            GlobeMessage(messageModel = it)
        }
    }
}













