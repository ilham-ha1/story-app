package org.dicoding.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import org.dicoding.storyapp.model.response.ListStoryItem
import javax.inject.Inject

@HiltViewModel
class HomeViewModel@Inject constructor(private val homeRepository: HomeRepository) : ViewModel() {

    val isLoading = homeRepository.isLoading
    val isDataNotFound = homeRepository.isDataNotFound

    fun requestListStory(token: String): LiveData<PagingData<ListStoryItem>> =
        homeRepository.requestListStory(token).cachedIn(viewModelScope)

}