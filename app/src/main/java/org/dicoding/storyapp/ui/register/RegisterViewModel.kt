package org.dicoding.storyapp.ui.register

import android.content.Context
import androidx.lifecycle.ViewModel
import org.dicoding.storyapp.model.body.RegisterBody
import org.dicoding.storyapp.model.preference.UserPreference

class RegisterViewModel(context: Context, private val pref: UserPreference) : ViewModel() {
    private val registerRepository:RegisterRepository = RegisterRepository(context,pref)

    val isLoading = registerRepository.isLoading
    val success = registerRepository.success

    fun requestRegister(registerBody: RegisterBody) {
        registerRepository.requestRegister(registerBody)
    }
}