package com.weather.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.data.common.Result
import com.data.model.City
import com.weather.R
import com.weather.components.RvHourlyWeatherAdapter
import com.weather.databinding.FragmentCityItemBinding
import com.weather.viewmodel.CityItemViewModel
import com.weather.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_city_item.*
import kotlinx.android.synthetic.main.layout_current_weather_top.*
import kotlin.math.roundToInt

private const val ARG_CITY_REGEX = "cityId"

class CityItemFragment : BaseFragment() {
    override val viewModel: CityItemViewModel by viewModels {
        ViewModelFactory(
            "CityItemViewModel",
            requireActivity().application,
            City.createCityByRegex(requireArguments().getString(ARG_CITY_REGEX)!!)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentCityItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_city_item, container, false)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()

    }

    private fun initComponents() {
        initSwipe()
        initHourlyRecycler()
    }

    private fun initSwipe() {
        swipe_city_item_to_refresh_weather_data.setOnRefreshListener {
            viewModel.refreshWeatherData()
        }
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.currentLD.observe(viewLifecycleOwner) {
            if (it is Result.Success) {
                tv_current_weather_temp.text = "${it.data.temp.roundToInt()} °"
                tv_current_weather_feels.text = "ощущается как ${it.data.feels_like.roundToInt()}"
                tv_current_weather_status.text = it.data.description
            }
        }
    }

    private fun initHourlyRecycler() {
        with(rv_item_hourly) {
            adapter = RvHourlyWeatherAdapter()
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(cityRegex: String) =
            CityItemFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CITY_REGEX, cityRegex)
                }
            }
    }
}