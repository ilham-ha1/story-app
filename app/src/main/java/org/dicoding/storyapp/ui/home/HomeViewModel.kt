package org.dicoding.storyapp.ui.home

import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    private val homeRepository: HomeRepository = HomeRepository()

    val listStory = homeRepository.listStory
    val isLoading = homeRepository.isLoading
    val isDataNotFound = homeRepository.isDataNotFound

    fun requestListStory(token: String){
       homeRepository.requestListStory(token)
    }

}