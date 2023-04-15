package org.dicoding.storyapp.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.dicoding.storyapp.model.body.LoginBody
import org.dicoding.storyapp.model.preference.UserModel
import org.dicoding.storyapp.model.preference.UserPreference

class LoginViewModel(context: Context, private val pref: UserPreference) : ViewModel() {
   private val loginRepository:LoginRepository = LoginRepository(context,pref)

    val isLoading = loginRepository.isLoading
    val isLogin = loginRepository.isLogin

    fun getUser(): LiveData<UserModel> {
        return loginRepository.getUser()
    }

    fun login() {
        viewModelScope.launch {
            loginRepository.login()
        }
    }

    fun requestLogin(loginBody: LoginBody){
        loginRepository.requestLogin(loginBody)
    }

}