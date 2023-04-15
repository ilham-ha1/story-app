package org.dicoding.storyapp.ui.detail

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.dicoding.storyapp.model.response.DetailStoryResponse
import org.dicoding.storyapp.remote.ApiConfig
import org.dicoding.storyapp.remote.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailRepository {
    private var apiService: ApiService = ApiConfig.getApiService()

    private val _detailStory = MutableLiveData<DetailStoryResponse>()
    val detailStory = _detailStory

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isDataNotFound = MutableLiveData(false)
    val isDataNotFound: LiveData<Boolean> = _isDataNotFound

    fun getDetailStory(id: String, token: String){
        _isLoading.value = true
        val client = apiService.getDetailStories("Bearer $token",id)
        client.enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                _isLoading.value = false
                _isDataNotFound.value = false
                if(response.isSuccessful){
                    _isDataNotFound.value = false
                    _detailStory.value = response.body()
                }else{
                    _isDataNotFound.value = true
                    Log.d(ContentValues.TAG, "Response get detail story is Failed: ${response.message()}")
                }

            }
            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                _isLoading.value = false
                _isDataNotFound.value = true
                Log.d(ContentValues.TAG, "Request get detail story is Failed: ${t.message}")
            }
        })
    }
}