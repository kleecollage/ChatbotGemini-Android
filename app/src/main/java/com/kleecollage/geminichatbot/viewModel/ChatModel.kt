package com.kleecollage.geminichatbot.viewModel

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "chatbot")
data class ChatModel (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val chat: String,

    val role: String,
)