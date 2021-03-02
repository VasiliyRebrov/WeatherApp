package com.weather.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.data.common.Result
import com.google.android.material.tabs.TabLayoutMediator
import com.weather.R
import com.weather.common.adapters.ViewPagerAdapter
import com.weather.common.components.awaitLayoutChange
import com.weather.databinding.FragmentGeneralBinding
import com.weather.viewmodel.GeneralViewModel
import com.weather.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_general.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

class GeneralFragment : BaseFragment() {
    private var _binding: FragmentGeneralBinding? = null
    private val binding get() = _binding!!

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
        _binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_general, container, false
            )
        binding.lifecycleOwner = viewLifecycleOwner
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
    }

    private fun initBar() {
        with(requireActivity() as AppCompatActivity) {
            setSupportActionBar(tb_general)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        tb_general.setNavigationIcon(R.drawable.baseline_list_white_24dp)
    }

    private fun initPager() {
        //если не исп локальный лайфсайкл - утечка
        val adapter = ViewPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)


        pager_general.offscreenPageLimit = 10
        pager_general.adapter = adapter
        pager_general.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                if (position != 0) sharedViewModel.focusedCityPos = position
                tb_general.title = adapter.getFocusedCityName(position)
            }
        })
        TabLayoutMediator(tab_general, pager_general) { _, _ -> }.attach()
        lifecycleScope.launch {
            pager_general.awaitLayoutChange {
                pager_general.currentItem = sharedViewModel.focusedCityPos
            }
        }
    }


    override fun onDestroyView() {
        pager_general.adapter = null
        _binding = null
        super.onDestroyView()
    }
}