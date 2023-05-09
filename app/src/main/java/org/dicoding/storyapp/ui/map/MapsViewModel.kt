package org.dicoding.storyapp.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.dicoding.storyapp.model.response.ListStoryItem


class MapsViewModel: ViewModel()  {
    private val mapsRepository = MapsRepository()

    fun reqListMap(token: String): LiveData<List<ListStoryItem>> =
        mapsRepository.reqListMap(token)

    fun getList():LiveData<List<ListStoryItem>> = mapsRepository.getList()

}