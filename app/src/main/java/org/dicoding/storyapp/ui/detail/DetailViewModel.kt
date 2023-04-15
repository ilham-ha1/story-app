package org.dicoding.storyapp.ui.detail

import androidx.lifecycle.ViewModel

class DetailViewModel: ViewModel() {
    private val detailRepository = DetailRepository()

    val detailStory = detailRepository.detailStory
    val isLoading = detailRepository.isLoading
    val isNotFound = detailRepository.isDataNotFound

    fun getDetailStory(id: String, token: String){
        detailRepository.getDetailStory(token,id)
    }
}