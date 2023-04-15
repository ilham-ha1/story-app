package org.dicoding.storyapp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.dicoding.storyapp.model.preference.UserModel
import org.dicoding.storyapp.model.preference.UserPreference

class MainViewModel(private val context: Context,private val pref: UserPreference) : ViewModel() {
    private val mainRepository:MainRepository = MainRepository(context,pref)

    fun getUser(): LiveData<UserModel> {
        return mainRepository.getUser()
    }

    fun logout() {
        viewModelScope.launch {
            mainRepository.logout()
        }
    }

}