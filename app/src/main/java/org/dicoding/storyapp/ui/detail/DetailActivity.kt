package org.dicoding.storyapp.ui.detail

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.dicoding.storyapp.R
import org.dicoding.storyapp.databinding.ActivityDetailBinding
import org.dicoding.storyapp.model.preference.UserPreference


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class DetailActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailViewModel =
            ViewModelProvider(this).get(DetailViewModel::class.java)
        userPreference = UserPreference.getInstance(dataStore)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detail story"

        val id = intent.getStringExtra(EXTRA_ID)

        lifecycleScope.launch {
            if (id != null) {
                detailViewModel.getDetailStory(userPreference.getToken().first(),id)
            }
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.isNotFound.observe(this){
            showNotFound(it)
        }

        detailViewModel.detailStory.observe(this){
            with(binding) {
                Glide.with(binding.root.context)
                    .load(it.story.photoUrl)
                    .into(binding.imageView2)
                tvDetailUsername.text = it.story.name
                tvDetailDescTitle.text = resources.getString(R.string.detail_desc_title)
                tvDetailDesc.text = it.story.description
                tvDetailDateTile.text = resources.getString(R.string.upload_date)
                tvDetailDate.text = it.story.createdAt
                tvDetailPositionTile.text = resources.getString(R.string.position)
                val positionText = if (it.story.lat != null && it.story.lon != null) {
                    "${it.story.lat}, ${it.story.lon}"
                } else {
                    ""
                }
                tvDetailPosition.text = positionText
            }
        }
    }

    private fun showNotFound(isNotFound: Boolean) {
        binding.notFound.visibility = if (isNotFound) View.VISIBLE else View.GONE
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
        binding.progressBarDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object{
        const val EXTRA_ID = "extra_id"
    }
}