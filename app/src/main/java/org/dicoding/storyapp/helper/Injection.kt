package org.dicoding.storyapp.helper

import android.content.Context
import org.dicoding.storyapp.model.StoryDatabase
import org.dicoding.storyapp.remote.ApiConfig
import org.dicoding.storyapp.ui.home.HomeRepository

object Injection {
    fun homeRepository(context:Context):HomeRepository{
        val service = ApiConfig.getApiService()
        val db = StoryDatabase.getDatabase(context)
        return HomeRepository(db,service)
    }
}