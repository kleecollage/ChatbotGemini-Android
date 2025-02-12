package com.kleecollage.geminichatbot.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kleecollage.geminichatbot.viewModel.ChatModel

@Database(entities = [ChatModel::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun chatDao(): ChatDao
}