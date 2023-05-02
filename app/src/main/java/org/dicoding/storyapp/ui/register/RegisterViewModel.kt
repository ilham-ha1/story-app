package org.dicoding.storyapp.ui.register

import android.content.Context
import androidx.lifecycle.ViewModel
import org.dicoding.storyapp.model.body.RegisterBody
import org.dicoding.storyapp.model.preference.UserPreference

class RegisterViewModel(context: Context) : ViewModel() {
    private val registerRepository:RegisterRepository = RegisterRepository(context)

    val isLoading = registerRepository.isLoading
    val success = registerRepository.success

    fun requestRegister(registerBody: RegisterBody) {
        registerRepository.requestRegister(registerBody)
    }
}