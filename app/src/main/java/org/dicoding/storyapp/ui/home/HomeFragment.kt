package org.dicoding.storyapp.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.dicoding.storyapp.adapter.StoryAdapter
import org.dicoding.storyapp.databinding.FragmentHomeBinding
import org.dicoding.storyapp.model.preference.UserPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var userPreference:UserPreference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        userPreference = UserPreference.getInstance(requireContext().dataStore)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        lifecycleScope.launch {
            val token = userPreference.getToken().first()
            homeViewModel.requestListStory(token)
        }
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFrHome.layoutManager = layoutManager

        homeViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }

        homeViewModel.isDataNotFound.observe(viewLifecycleOwner){
            showNotFound(it)
        }

        homeViewModel.listStory.observe(viewLifecycleOwner){story->
            val adapter = StoryAdapter(story)
            binding.rvFrHome.adapter = adapter
        }
    }

    private fun showNotFound(isNotFound: Boolean) {
        binding.notFound.visibility = if (isNotFound) View.VISIBLE else View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}