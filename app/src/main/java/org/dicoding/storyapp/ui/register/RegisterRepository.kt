package org.dicoding.storyapp.ui.register

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.dicoding.storyapp.R
import org.dicoding.storyapp.model.body.RegisterBody
import org.dicoding.storyapp.model.preference.UserPreference
import org.dicoding.storyapp.model.response.RegisterResponse
import org.dicoding.storyapp.remote.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterRepository(private val context: Context) {
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _success = MutableLiveData(false)
    val success: LiveData<Boolean> = _success

    fun requestRegister(registerBody: RegisterBody) {
        _isLoading.value = true
        _success.value = false
        val service = ApiConfig.getApiService().registerUser(registerBody)
        service.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _success.value = true
                } else {
                    Toast.makeText(context, context.getString(R.string.failed_register), Toast.LENGTH_SHORT ).show()
                    Log.d("Response failed", "message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                Toast.makeText(context, context.getString(R.string.failed_register), Toast.LENGTH_SHORT ).show()
                Log.d("Register Request", "message: ${t.message}")
            }
        })
    }
}