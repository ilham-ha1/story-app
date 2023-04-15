package org.dicoding.storyapp.ui.home

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.dicoding.storyapp.model.response.GetAllStoryResponse
import org.dicoding.storyapp.model.response.ListStoryItem
import org.dicoding.storyapp.remote.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRepository {
    private var _listStory = MutableLiveData<List<ListStoryItem>>()
    val listStory = _listStory

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isDataNotFound = MutableLiveData(false)
    val isDataNotFound: LiveData<Boolean> = _isDataNotFound

    fun requestListStory(token: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllStories("Bearer $token")
        client.enqueue(object : Callback<GetAllStoryResponse> {
            override fun onResponse(
                call: Call<GetAllStoryResponse>,
                response: Response<GetAllStoryResponse>
            ) {
                _isLoading.value = false
                _isDataNotFound.value = false
                if(response.isSuccessful){
                    _isDataNotFound.value = false
                    _listStory.postValue(response.body()?.listStory)
                }else{
                    _isDataNotFound.value = true
                    Log.d(ContentValues.TAG, "Response get all story is Failed: ${response.message()}")
                }

            }
            override fun onFailure(call: Call<GetAllStoryResponse>, t: Throwable) {
                _isLoading.value = false
                _isDataNotFound.value = true
                Log.d(ContentValues.TAG, "Request get all story is Failed: ${t.message}")
            }
        })
    }
}