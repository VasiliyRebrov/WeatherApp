package com.weather.view

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.data.common.Result
import com.data.model.City
import com.google.android.material.tabs.TabLayoutMediator
import com.weather.R
import com.weather.common.adapters.ViewPagerAdapter
import com.weather.common.components.awaitLayoutChange
import com.weather.databinding.FragmentGeneralBinding
import com.weather.databinding.FragmentGeneralBindingImpl
import com.weather.viewmodel.AddCityViewModel
import com.weather.viewmodel.BaseViewModel
import com.weather.viewmodel.GeneralViewModel
import com.weather.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_general.*
import kotlinx.coroutines.launch

class GeneralFragment : BaseFragment() {
    override val model: GeneralViewModel
        get() = super.model as GeneralViewModel

    override val localCitiesObserver: (Result<List<City>>) -> Unit = {
        if (it is Result.Success && it.data.isEmpty())
            findNavController().navigate(R.id.action_generalFragment_to_citiesManagerFragment)
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

    override fun inflate(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_general, container, false
            )
        with(binding as FragmentGeneralBinding) {
            lifecycleOwner = viewLifecycleOwner
            this.model = this@GeneralFragment.model
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        initBar()
        initPager()
    }

    private fun initBar() {
        with(requireActivity() as AppCompatActivity) {
            setSupportActionBar(tb_general)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        tb_general.setNavigationIcon(R.drawable.baseline_list_white_24dp)
    }

    private fun initPager() {
        with(pager_general) {
            val adapter = ViewPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
            this.adapter = adapter
            offscreenPageLimit = 10
            registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    if (position != 0) sharedModel.focusedCityPos = position
                    tb_general.title = adapter.getFocusedCityName(position)
                }
            })
            TabLayoutMediator(tab_general, pager_general) { _, _ -> }.attach()
            lifecycleScope.launch {
                pager_general.awaitLayoutChange {
                    pager_general.currentItem = sharedModel.focusedCityPos
                }
            }
        }
    }

    override fun onDestroyView() {
        pager_general.adapter = null
        super.onDestroyView()
    }
}