package com.weather.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.data.common.*
import com.data.model.City
import com.weather.R
import com.weather.common.adapters.RecyclerRemoteCitiesAdapter
import com.weather.common.entities.DialogType
import com.weather.databinding.FragmentAddCityBinding
import com.weather.viewmodel.AddCityViewModel
import kotlinx.android.synthetic.main.fragment_add_city.*

private const val REQUEST_CODE_LOCATION_PERMISSION = 999

class AddCityFragment : BaseFragment() {
    override val model: AddCityViewModel
        get() = super.model as AddCityViewModel

    var localCitiesFirstChangeFlag = false
    override val localCitiesObserver: (Result<List<City>>) -> Unit = {
        if (it is Result.Success && it.data.isNotEmpty() && localCitiesFirstChangeFlag)
            findNavController().popBackStack()
        localCitiesFirstChangeFlag = true
    }

    override val errorEventObserver = Observer<Result.Error> {
        if (it.exception.cause is NetworkProviderDisabledException)
            showDialogFragment(DialogType.ENABLE_LOCATION)
        else if (it.exception.cause is CityAlreadyExistException)
            super.errorEventObserver.onChanged(it)
    }

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_city, container, false)
        with(binding as FragmentAddCityBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = model
        }
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            with(sharedModel.localCitiesByIdLD.value) {
                if (this is Result.Success && this.data.isNotEmpty()) findNavController().popBackStack()
                else requireActivity().finish()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        initBar()
        initInputField()
        initLocButton()
        initRecycler()
    }

    private fun initBar() {
        with(requireActivity() as AppCompatActivity) {
            setSupportActionBar(tb_add_city)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun initInputField() {
        et_add_city_find_by_name.addTextChangedListener {
            model.findCityByName(it.toString())
        }
    }

    private fun initLocButton() {
        but_add_city_by_loc.setOnClickListener {
            defineLoc()
        }
    }

    private fun initRecycler() {
        with(recycler_add_city_result) {
            adapter = RecyclerRemoteCitiesAdapter {
                model.addCity(it)
            }
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun defineLoc() {
        val permissionCheck = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permissionCheck == PackageManager.PERMISSION_GRANTED)
            model.defineLoc()
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
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    model.defineLoc()
                else if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
                    showDialogFragment(DialogType.ALLOW_LOCATION_PERMISSION)
            }
        }
    }
}