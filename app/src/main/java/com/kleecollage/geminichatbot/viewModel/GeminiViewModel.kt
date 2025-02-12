package com.kleecollage.geminichatbot.viewModel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.kleecollage.geminichatbot.BuildConfig
import com.kleecollage.geminichatbot.model.MessageModel
import com.kleecollage.geminichatbot.room.AppDatabase
import kotlinx.coroutines.launch

class GeminiViewModel(application: Application): AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "chat_bot"
    ).build()


    private val generativeModel = GenerativeModel (
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey
    )

    private val chat by lazy {
        generativeModel.startChat()
    }

    val messageList by lazy {
        mutableStateListOf<MessageModel>()
        // mutableStateListOf(MessageModel("Welcome to Chat Gemini!", role = "model"))
    }

/*    fun sendMessage(question: String) {
        viewModelScope.launch {
            try {
                messageList.add(MessageModel(question, role = "user"))
                val response = chat.sendMessage(question)
                messageList.add(MessageModel(response.text.toString(), role = "model"))
            } catch (e: Exception) {
                messageList.add(MessageModel("Error on the conversation: ${e.message}", role = "model"));
            }
        }
    }*/

    // ROOM
    fun sendMessage(question: String) {
        viewModelScope.launch {
            try {
                messageList.add(MessageModel(question, role = "user"))
                val contextChat = messageList.joinToString(separator = "\n") {"${it.role}: ${it.message}"}
                val response = chat.sendMessage(contextChat)
                messageList.add(MessageModel(response.text.toString(), role = "model"))
                // ROOM
                val chatDao = db.chatDao()
                chatDao.insertChat(item = ChatModel(chat = question, role = "user"))
                chatDao.insertChat(item = ChatModel(chat = response.text.toString(), role = "model"))
            } catch (e: Exception) {
                messageList.add(MessageModel("Error on the conversation: ${e.message}", role = "model"));
            }
        }
    }

    fun loadChat() {
        try {
            viewModelScope.launch {
                val chatDao = db.chatDao()
                val savedChat = chatDao.getChat()
                messageList.clear()
                for (chat in savedChat) {
                    messageList.add(MessageModel(message = chat.chat, role = chat.role))
                }
            }
        } catch (e: Exception) {
            messageList.add(MessageModel("Error on load chat: ${e.message}", role = "model"));
        }
    }

    fun deleteChat() {
        viewModelScope.launch {
            try {
                val chatDao = db.chatDao()
                chatDao.deleteChat()
                messageList.clear()
            } catch (e: Exception) {
                messageList.add(MessageModel("Error on deleting chat: ${e.message}", role = "model"));
            }
        }
    }

    // SEND IMAGE TO GEMINI
    var descriptionResponse by mutableStateOf("")
        private set

    var image by mutableStateOf<Uri>(Uri.EMPTY)

    fun descriptionImage(bitmap: Bitmap) {
        viewModelScope.launch {
            try {
                val inputContent = content {
                    image(bitmap)
                    text("Describe this image")
                }
                val response = generativeModel.generateContent(inputContent)
                descriptionResponse = response.text.toString()
            } catch (e: Exception) {
                descriptionResponse = "Error sending image"
            }
        }
    }

    fun cleanVars() {
        descriptionResponse = ""
        image = Uri.EMPTY
    }

}










