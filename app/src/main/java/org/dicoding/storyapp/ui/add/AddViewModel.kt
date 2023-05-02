package org.dicoding.storyapp.ui.add

import android.content.Context
import androidx.lifecycle.ViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.dicoding.storyapp.model.preference.UserPreference

class AddViewModel(private val context: Context): ViewModel() {

    private val addRepository:AddRepository = AddRepository(context)

    fun uploadImg(token: String, multiPart: MultipartBody.Part, description: RequestBody){
        addRepository.uploadImg(token,multiPart,description)
    }


}