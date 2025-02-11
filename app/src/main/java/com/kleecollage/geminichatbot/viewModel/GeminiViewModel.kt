package com.kleecollage.geminichatbot.viewModel

import androidx.lifecycle.ViewModel
import com.google.ai.client.generativeai.GenerativeModel
import com.kleecollage.geminichatbot.BuildConfig

class GeminiViewModel: ViewModel() {
    private val generativeModel = GenerativeModel (
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey
    )
}