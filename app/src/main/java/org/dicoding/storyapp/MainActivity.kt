package org.dicoding.storyapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.dicoding.storyapp.databinding.ActivityMainBinding
import org.dicoding.storyapp.factory.ViewModelFactory
import org.dicoding.storyapp.model.preference.UserPreference
import org.dicoding.storyapp.ui.welcome.WelcomeActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(this,UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)?.findNavController()

        val navView: BottomNavigationView = binding.navView
        if (navController != null) {
            navView.setupWithNavController(navController)
        }

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_add, R.id.navigation_setting
            )
        )

        if (navController != null) {
            setupActionBarWithNavController(navController,appBarConfiguration)
        }

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
            }
        }
    }
}