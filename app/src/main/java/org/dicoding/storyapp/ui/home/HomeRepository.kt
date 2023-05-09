package org.dicoding.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import org.dicoding.storyapp.model.StoryDatabase
import org.dicoding.storyapp.model.StoryRemoteMediator
import org.dicoding.storyapp.model.response.ListStoryItem
import org.dicoding.storyapp.remote.ApiService
import javax.inject.Inject


class HomeRepository@Inject constructor(private val database: StoryDatabase, private val apiService: ApiService){

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isDataNotFound = MutableLiveData(false)
    val isDataNotFound: LiveData<Boolean> = _isDataNotFound

    @OptIn(ExperimentalPagingApi::class)
    fun requestListStory(token:String):LiveData<PagingData<ListStoryItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            remoteMediator = StoryRemoteMediator(database, apiService,token, _isLoading, _isDataNotFound),
            pagingSourceFactory = {
                database.listDao().getAllList()
            }
        ).liveData
    }

}