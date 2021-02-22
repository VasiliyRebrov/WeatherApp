package com.weather.view

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.data.common.Result
import com.google.android.material.tabs.TabLayoutMediator
import com.weather.R
import com.weather.components.ViewPagerAdapter
import com.weather.databinding.FragmentGeneralBinding
import com.weather.viewmodel.BaseViewModel
import com.weather.viewmodel.GeneralViewModel
import com.weather.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_general.*

class GeneralFragment : BaseFragment() {
    override val viewModel: GeneralViewModel by viewModels {
        ViewModelFactory("GeneralViewModel", requireActivity().application)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigate(R.id.action_generalFragment_to_citiesManagerFragment)
                true
            }
            R.id.action_settings -> {
                findNavController().navigate(R.id.action_generalFragment_to_settingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_general, menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedViewModel.localCitiesLiveData.observe(viewLifecycleOwner) {
            if (it is Result.Success && it.data.isEmpty())
                findNavController().navigate(R.id.action_generalFragment_to_citiesManagerFragment)
        }
//        checkExistCitiesList(R.id.action_generalFragment_to_citiesManagerFragment)
        val binding: FragmentGeneralBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_general, container, false
            )
        binding.lifecycleOwner = this
        binding.model = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        initBar()
        initPager()
        initObservers()
    }

    private fun initObservers() {

    }

    private fun initBar() {
        with(requireActivity() as AppCompatActivity) {
            setSupportActionBar(tb_general)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        tb_general.setNavigationIcon(R.drawable.baseline_list_white_24dp)
    }

    private fun initPager() {
        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        pager_general.adapter = adapter
        TabLayoutMediator(tab_general, pager_general) { _, _ ->
        }.attach()
    }
}