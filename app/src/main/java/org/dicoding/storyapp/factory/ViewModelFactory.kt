package org.dicoding.storyapp.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.dicoding.storyapp.MainViewModel
import org.dicoding.storyapp.helper.Injection
import org.dicoding.storyapp.model.preference.UserPreference
import org.dicoding.storyapp.ui.add.AddViewModel
import org.dicoding.storyapp.ui.detail.DetailViewModel
import org.dicoding.storyapp.ui.home.HomeViewModel
import org.dicoding.storyapp.ui.login.LoginViewModel
import org.dicoding.storyapp.ui.map.MapsViewModel
import org.dicoding.storyapp.ui.register.RegisterViewModel

class ViewModelFactory(private val context: Context,private val pref: UserPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(context,pref) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(Injection.homeRepository(context) ) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel() as T
            }
            modelClass.isAssignableFrom(AddViewModel::class.java) -> {
                AddViewModel(context) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(context) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(context,pref) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}