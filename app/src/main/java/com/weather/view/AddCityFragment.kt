package com.weather.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.data.common.data
import com.weather.R
import com.weather.databinding.FragmentAddCityBinding
import com.weather.viewmodel.AddCityViewModel
import com.weather.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_add_city.*

private const val REQUEST_CODE_LOCATION_PERMISSION = 999

class AddCityFragment : BaseFragment() {
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
        viewModel.findCityUseCaseLiveData.observe(viewLifecycleOwner) {
            Log.d("MyTag", "FIND UC: $it | ${it.data ?: "NULL"}")

        }
        viewModel.defineLocationUseCaseLiveData.observe(viewLifecycleOwner) {
            Log.d("MyTag", " LOCATION UC: $it | ${it.data ?: "NULL"}")
        }
        viewModel.addCityByLocationUseCaseLiveData.observe(viewLifecycleOwner) {
            Log.d("MyTag", " ADD BY LOCATION UC: $it | ${it.data ?: "NULL"}")
        }
        viewModel.progressLiveData.observe(viewLifecycleOwner) {
            Log.d("MyTag", "PROGRESS STATUS: $it")
        }
        viewModel.errorEvent.observe(viewLifecycleOwner) {
            Log.d("MyTag", "ERROR: $it")
        }
//        viewModel.errorEvent.observe(viewLifecycleOwner) {
//            Log.d("MyTag", "ERROR: $it")
//        }
        initButton()
    }

    private fun initBar() {
        with(requireActivity() as AppCompatActivity) {
            setSupportActionBar(tb_add_city)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun initButton() {
        but_add_city_find_by_location.setOnClickListener {
            checkAndRequestGeoPermission()
        }
    }

    private fun checkAndRequestGeoPermission() {
        val permissionCheck = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permissionCheck == PackageManager.PERMISSION_GRANTED)
            viewModel.defineLocation()
        else
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION_PERMISSION
            )

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()) {
            if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) viewModel.defineLocation()
                else if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
                    showDialogFragment(DialogAlertType.ALLOW_LOCATION_PERMISSION)
            }
        }
    }
}