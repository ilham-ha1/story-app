package org.dicoding.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import org.dicoding.storyapp.MainActivity
import org.dicoding.storyapp.databinding.ActivityLoginBinding
import org.dicoding.storyapp.factory.ViewModelFactory
import org.dicoding.storyapp.model.body.LoginBody
import org.dicoding.storyapp.model.preference.UserModel
import org.dicoding.storyapp.model.preference.UserPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var user: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
        setupAnimation()
    }

    private fun setupAnimation() {
        val logo = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1F).setDuration(500)
        val titleText = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1F).setDuration(500)
        val messageText = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1F).setDuration(500)
        val emailText = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1F).setDuration(500)
        val emailLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout,View.ALPHA,1F).setDuration(500)
        val passText = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1F).setDuration(500)
        val passLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout,View.ALPHA,1F).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1F).setDuration(500)

        val anim = AnimatorSet()
        anim.playSequentially(logo,titleText,messageText,emailText,emailLayout,passText,passLayout,login)
        anim.startDelay = 500

        anim.start()
    }

    private fun setupView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.title = ""
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(this,UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this) { user ->
            this.user = user
        }
    }

    private fun setupAction() {
        loginViewModel.isLoading.observe(this){
            showLoading(it)
        }
        binding.loginButton.isEnabled = false

        binding.emailEditText.addTextChangedListener(textWatcher)
        binding.passwordEditText.addTextChangedListener(textWatcher)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val loginData = LoginBody(
                email, password
            )
            loginViewModel.requestLogin(loginData)
            loginViewModel.isLogin.observe(this){
                if(it.equals(true)){
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            binding.loginButton.isEnabled =
                        !binding.emailEditText.text.isNullOrBlank() &&
                        !binding.passwordEditText.text.isNullOrBlank()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}