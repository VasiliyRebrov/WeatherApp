package com.weather.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.data.common.NoNetworkException
import com.data.common.Result
import com.data.model.City
import com.domain.usecases.RefreshDataUC
import com.weather.R
import com.weather.common.adapters.RvDailyWeatherAdapter
import com.weather.common.adapters.RvHourlyWeatherAdapter
import com.weather.databinding.FragmentCityItemBinding
import com.weather.viewmodel.CityItemViewModel
import com.weather.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_city_item.*

private const val ARG_CITY_REGEX = "cityId"

class CityItemFragment : BaseFragment() {
    override val model: CityItemViewModel by viewModels {
        ViewModelFactory(
            this::class.java.simpleName,
            requireActivity().application,
            City.createCityByRegex(requireArguments().getString(ARG_CITY_REGEX)!!)
        )
    }

    override val errorEventObserver: Observer<Result.Error> = Observer {
        if (it.exception.cause is NoNetworkException)
            super.errorEventObserver.onChanged(it)
    }

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_city_item, container, false)
        with(binding as FragmentCityItemBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = model
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()

    }

    private fun initComponents() {
        initSwipe()
        initHourlyListRecycler()
        initDailyListRecycler()

    }

    private fun initSwipe() {
        swipe_city_item_to_refresh_weather_data.setOnRefreshListener {
            model.refreshData()
        }
    }

    private fun initHourlyListRecycler() {
        with(rv_item_hourly) {
            adapter = RvHourlyWeatherAdapter()
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }


    private fun initDailyListRecycler() {
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
}