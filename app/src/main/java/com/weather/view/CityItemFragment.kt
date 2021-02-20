package com.weather.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.data.model.City
import com.weather.R
import com.weather.databinding.FragmentCityItemBinding
import com.weather.viewmodel.BaseViewModel
import com.weather.viewmodel.CityItemViewModel
import com.weather.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_city_item.*

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
        swipe_city_item_to_refresh_weather_data.setOnRefreshListener {
            viewModel.refreshWeatherData()
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