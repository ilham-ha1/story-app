package org.dicoding.storyapp.ui.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.dicoding.storyapp.model.response.GetAllStoryResponse
import org.dicoding.storyapp.model.response.ListStoryItem
import org.dicoding.storyapp.remote.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsRepository {
    private val _listStoryMap = MutableLiveData<List<ListStoryItem>>()

    fun reqListMap(token: String): LiveData<List<ListStoryItem>>{
        ApiConfig.getApiService().getStoriesMap("Bearer $token",1)
            .enqueue(object : Callback<GetAllStoryResponse>{
                override fun onResponse(call: Call<GetAllStoryResponse>, response: Response<GetAllStoryResponse>) {
                    if (response.isSuccessful){
                        _listStoryMap.postValue(response.body()?.listStory)
                    }
                }
                override fun onFailure(call: Call<GetAllStoryResponse>, t: Throwable) {
                    Log.d("MAP REPO","onFailure enqueue")
                }
        })
        return _listStoryMap
    }

    fun getList(): LiveData<List<ListStoryItem>>{
        return _listStoryMap
    }
}