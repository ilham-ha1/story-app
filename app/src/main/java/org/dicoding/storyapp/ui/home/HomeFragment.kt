package org.dicoding.storyapp.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.dicoding.storyapp.R
import org.dicoding.storyapp.adapter.StoryAdapter
import org.dicoding.storyapp.databinding.FragmentHomeBinding
import org.dicoding.storyapp.factory.ViewModelFactory
import org.dicoding.storyapp.model.preference.UserPreference
import org.dicoding.storyapp.ui.map.MapsActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class HomeFragment : Fragment(){

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var userPreference:UserPreference
    private lateinit var token:String
    private lateinit var adapter: StoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(requireContext(), UserPreference.getInstance(requireContext().dataStore))
        )[HomeViewModel::class.java]

        userPreference = UserPreference.getInstance(requireContext().dataStore)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        adapter = StoryAdapter()

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        lifecycleScope.launch {
            token = userPreference.getToken().first()
            setupRecyclerView()
        }
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

        binding.rvFrHome.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter { adapter.retry() }
        )

        homeViewModel.requestListStory(token).observe(viewLifecycleOwner){ pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }

    }

    private fun showNotFound(isNotFound: Boolean) {
        binding.notFound.visibility = if (isNotFound) View.VISIBLE else View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.maps->{
                val intent = Intent(requireContext(), MapsActivity::class.java)
                startActivity(intent)

                val layoutManager = binding.rvFrHome.layoutManager as LinearLayoutManager
                layoutManager.scrollToPositionWithOffset(0, 0)
            }
        }
        return super.onContextItemSelected(item)
    }

}