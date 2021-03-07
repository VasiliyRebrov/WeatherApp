package com.weather.view

import android.database.DataSetObserver
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.data.common.Result
import com.data.model.City
import com.weather.R
import com.weather.common.adapters.PagerAdapter
import com.weather.databinding.FragmentGeneralBinding
import com.weather.viewmodel.GeneralViewModel
import kotlinx.android.synthetic.main.fragment_general.*

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
            val adapter = PagerAdapter(childFragmentManager)
            this.adapter = adapter
            tab_general.setupWithViewPager(this)
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    sharedModel.focusedCityPos = adapter.cities[position].position
                    updateTitle(adapter.cities)
                }

                override fun onPageScrollStateChanged(state: Int) {
                }

            })
            adapter.registerDataSetObserver(object : DataSetObserver() {
                override fun onChanged() {
                    pager_general.currentItem = sharedModel.focusedCityPos
                    updateTitle(adapter.cities)
                }
            })

        }
    }

    fun updateTitle(cities: List<City>) {
        (requireActivity() as AppCompatActivity).supportActionBar!!.title =
            cities[sharedModel.focusedCityPos].name
    }

    override fun onDestroyView() {
        pager_general.adapter = null
        super.onDestroyView()
    }
}
