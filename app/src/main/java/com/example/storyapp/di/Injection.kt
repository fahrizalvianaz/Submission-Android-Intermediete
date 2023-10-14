package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.data.local.database.StoryDatabase
import com.example.storyapp.data.remote.network.ApiConfig
import com.example.storyapp.data.repository.StoryAppRepository

object Injection {
    fun provideRepository(context: Context): StoryAppRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryAppRepository(database,apiService)
    }
}