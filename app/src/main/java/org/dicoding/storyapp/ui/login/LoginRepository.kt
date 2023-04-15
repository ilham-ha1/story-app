package org.dicoding.storyapp.ui.login

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.dicoding.storyapp.R
import org.dicoding.storyapp.model.body.LoginBody
import org.dicoding.storyapp.model.preference.UserModel
import org.dicoding.storyapp.model.preference.UserPreference
import org.dicoding.storyapp.model.response.LoginResponse
import org.dicoding.storyapp.remote.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository(private val context: Context, private val pref: UserPreference) {
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLogin = MutableLiveData(false)
    val isLogin: LiveData<Boolean> = _isLogin

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    suspend fun login() {
        pref.login()
    }

    fun saveUser(user: UserModel) {
        CoroutineScope(Dispatchers.IO).launch {
            pref.saveUser(user)
        }
    }

    fun requestLogin(loginBody: LoginBody){
        _isLoading.value = true
        _isLogin.value = false
        val client = ApiConfig.getApiService().loginUser(loginBody)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if(response.isSuccessful){
                    _isLogin.value = true
                    val login = response.body()?.loginResult
                    val model = UserModel(
                        login!!.name, loginBody.email, loginBody.password, true, login.token
                    )
                    saveUser(model)
                }else{
                    _isLogin.value = false
                    Toast.makeText(context, context.getString(R.string.failed_login), Toast.LENGTH_SHORT ).show()
                    Log.d(ContentValues.TAG, "Response is failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _isLogin.value = false
                Toast.makeText(context,  context.getString(R.string.failed_login), Toast.LENGTH_SHORT ).show()
                Log.d(ContentValues.TAG, "Request Login is Failed: ${t.message}")
            }

        })
    }

}