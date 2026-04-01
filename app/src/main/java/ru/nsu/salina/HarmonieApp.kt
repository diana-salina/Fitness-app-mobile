package ru.nsu.salina

import android.app.Application
import ru.nsu.salina.data.local.AppDatabase
import ru.nsu.salina.data.remote.RetrofitClient
import ru.nsu.salina.data.repository.TrainingRepository
import ru.nsu.salina.data.repository.TrainingRepositoryImpl

class HarmonieApp : Application() {
    val repository: TrainingRepository by lazy {
        TrainingRepositoryImpl(
            db = AppDatabase.getInstance(this),
            apiService = RetrofitClient.apiService
        )
    }
}
