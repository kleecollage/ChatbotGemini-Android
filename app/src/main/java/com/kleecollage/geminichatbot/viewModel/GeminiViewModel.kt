package com.kleecollage.geminichatbot.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.kleecollage.geminichatbot.BuildConfig
import com.kleecollage.geminichatbot.model.MessageModel
import kotlinx.coroutines.launch

class GeminiViewModel: ViewModel() {
    private val generativeModel = GenerativeModel (
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey
    )

    private val chat by lazy {
        generativeModel.startChat()
    }

    val messageList by lazy {
        // mutableStateListOf<MessageModel>()
        mutableStateListOf(MessageModel("Welcome to Chat Gemini!", role = "model"))
    }

    fun sendMessage(question: String) {
        viewModelScope.launch {
            try {
                messageList.add(MessageModel(question, role = "user"))
                val response = chat.sendMessage(question)
                messageList.add(MessageModel(response.text.toString(), role = "model"))
            } catch (e: Exception) {
                messageList.add(MessageModel("Error on the conversation: ${e.message}", role = "model"));
            }
        }
    }
}










