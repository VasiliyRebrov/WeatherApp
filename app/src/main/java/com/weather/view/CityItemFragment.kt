package com.weather.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.data.model.City
import com.weather.R
import com.weather.common.adapters.RvDailyWeatherAdapter
import com.weather.common.adapters.RvHourlyWeatherAdapter
import com.weather.databinding.FragmentCityItemBinding
import com.weather.viewmodel.CityItemViewModel
import com.weather.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_city_item.*

private const val ARG_CITY_REGEX = "cityId"

class CityItemFragment : BaseFragment() {
    val kek: String
        get() = City.createCityByRegex(requireArguments().getString(ARG_CITY_REGEX)!!).name

    private var _binding: FragmentCityItemBinding? = null
    private val binding get() = _binding!!
    override val viewModel: CityItemViewModel by viewModels {
        ViewModelFactory(
            "CityItemViewModel",
            requireActivity().application,
            City.createCityByRegex(requireArguments().getString(ARG_CITY_REGEX)!!)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("qweewq", "onCreate | $kek")

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_city_item, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel
        Log.d("qweewq", "onCreateView | $kek")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()

    }

    private fun initComponents() {
        initSwipe()
        initHourlyRecycler()
        initDailyRecycler()

    }

    private fun initSwipe() {
        swipe_city_item_to_refresh_weather_data.setOnRefreshListener {
            viewModel.refreshWeatherData()
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

    override fun onStart() {
        Log.d("qweewq", "onStart | $kek")

        super.onStart()
    }

    override fun onResume() {
        Log.d("qweewq", "onResume | $kek")

        super.onResume()
    }

    private fun initDailyRecycler() {
        with(rv_item_daily) {
            adapter = RvDailyWeatherAdapter()
            layoutManager = LinearLayoutManager(requireContext())
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

    override fun onStop() {
        Log.d("qweewq", "onStop | $kek")
        super.onStop()
    }

    override fun onPause() {
        Log.d("qweewq", "onPause | $kek")
        super.onPause()
    }

    override fun onDestroyView() {
        Log.d("qweewq", "onDestroyView | $kek")
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        Log.d("qweewq", "onDestroy | $kek")
        super.onDestroy()
    }
}