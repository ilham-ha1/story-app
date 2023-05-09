package org.dicoding.storyapp.ui.add

import android.content.Context
import android.content.Intent
import android.widget.Toast
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.dicoding.storyapp.MainActivity
import org.dicoding.storyapp.R
import org.dicoding.storyapp.model.response.AddNewStoryResponse
import org.dicoding.storyapp.remote.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddRepository (private val context: Context){

    fun uploadImg(token: String, multiPart: MultipartBody.Part, description: RequestBody){
        val service = ApiConfig.getApiService().uploadImage(token, description, multiPart)
        service.enqueue(object : Callback<AddNewStoryResponse> {
            override fun onResponse(
                call: Call<AddNewStoryResponse>,
                response: Response<AddNewStoryResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        Toast.makeText(context, responseBody.message, Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                    }
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<AddNewStoryResponse>, t: Throwable) {
                Toast.makeText(context, context.getString(R.string.failed_instance_retrofit), Toast.LENGTH_SHORT).show()
            }
        })
    }

}