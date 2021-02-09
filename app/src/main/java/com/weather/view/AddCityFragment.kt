package com.weather.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.data.common.AddCityUseCases
import com.data.common.NetworkProviderDisabledException
import com.data.common.data
import com.weather.R
import com.weather.databinding.FragmentAddCityBinding
import com.weather.viewmodel.AddCityViewModel
import com.weather.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_add_city.*

private const val REQUEST_CODE_LOCATION_PERMISSION = 999

class AddCityFragment : Fragment() {
    private val viewModel: AddCityViewModel by viewModels {
        ViewModelFactory("AddCityViewModel", requireActivity().application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentAddCityBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_city, container, false)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        initBar()
        et_add_city_find_by_name.addTextChangedListener {
            viewModel.search(it.toString())
        }
        viewModel.myLD.observe(viewLifecycleOwner) {
            Log.d("MyTag", "$it | ${it.data}")
        }
//        et_add_city_find_by_name.addTextChangedListener {
//            viewModel.search(it.toString())
//        }
    }

    private fun initBar() {
        with(requireActivity() as AppCompatActivity) {
            setSupportActionBar(tb_add_city)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }
}