package org.dicoding.storyapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import org.dicoding.storyapp.model.preference.UserModel
import org.dicoding.storyapp.model.preference.UserPreference
import org.dicoding.storyapp.ui.welcome.WelcomeActivity

class MainRepository(private val context: Context,private val pref: UserPreference) {
    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    suspend fun logout() {
        pref.logout()
        val intent = Intent(context, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)

        (context as? Activity)?.finishAffinity()
    }
}