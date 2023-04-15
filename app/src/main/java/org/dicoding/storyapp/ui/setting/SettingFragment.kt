package org.dicoding.storyapp.ui.setting

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import org.dicoding.storyapp.MainViewModel
import org.dicoding.storyapp.R
import org.dicoding.storyapp.factory.ViewModelFactory
import org.dicoding.storyapp.model.preference.UserPreference


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class SettingFragment : PreferenceFragmentCompat() {
    private lateinit var mainViewModel: MainViewModel
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(requireContext(),UserPreference.getInstance(requireContext().dataStore))
        )[MainViewModel::class.java]

        findPreference<Preference>("logout_preferences")?.setOnPreferenceClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setTitle(context.getString(R.string.set_confirm))
                setMessage(context.getString(R.string.sure))
                setNegativeButton(context.getString(R.string.no)) { _, _ -> }
                setPositiveButton(context.getString(R.string.yes)) { _, _ ->
                    mainViewModel.logout()
                }
                create()
                show()
            }
            true
        }
    }
}