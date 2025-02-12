package com.kleecollage.geminichatbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kleecollage.geminichatbot.components.GlobeMessage
import com.kleecollage.geminichatbot.ui.theme.GeminiChatbotTheme
import com.kleecollage.geminichatbot.viewModel.GeminiViewModel
import com.kleecollage.geminichatbot.views.HomeView
import com.kleecollage.geminichatbot.views.ModalView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: GeminiViewModel by viewModels()
        setContent {
            GeminiChatbotTheme {
                HomeView(viewModel)
                // ModalView()
            }
        }
    }
}