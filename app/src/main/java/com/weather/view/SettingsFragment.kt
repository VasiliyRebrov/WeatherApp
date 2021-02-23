package com.weather.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceFragmentCompat
import com.weather.R
import com.weather.components.initBaseObservers
import com.weather.viewmodel.SettingsViewModel
import com.weather.viewmodel.ViewModelFactory

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private val viewModel: SettingsViewModel by viewModels {
        ViewModelFactory("SettingsViewModel", requireActivity().application)
    }
//    private val currentVal by lazy { findPreference<ListPreference>("temp_key")!!.value }


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
        viewLifecycleOwner.initBaseObservers(viewModel)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
//        sharedPreferences?.let { Log.d("aass", it.all.toString()) }
//        key?.let { Log.d("aass", it) }
        viewModel.transformData()
    }
}