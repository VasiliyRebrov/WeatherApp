package com.weather.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.data.common.InvalidArgsException
import com.data.common.Result
import com.data.common.data
import com.data.common.succeeded
import com.weather.R
import com.weather.components.DialogAlertType
import com.weather.components.RvRemoteCitiesAdapter
import com.weather.databinding.FragmentAddCityBinding
import com.weather.viewmodel.AddCityViewModel
import com.weather.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_add_city.*

private const val REQUEST_CODE_LOCATION_PERMISSION = 999

class AddCityFragment : BaseFragment() {
    override val viewModel: AddCityViewModel by viewModels {
        ViewModelFactory("AddCityViewModel", requireActivity().application)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
//            if (sharedViewModel.cities.value!!.isEmpty()) requireActivity().finish()
//            else
            findNavController().popBackStack()
            return true
        }
        return super.onOptionsItemSelected(item)
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
        initInputField()
        initButton()
        initRecycler()
    }

    override val eventObserver: Observer<Result<*>> = Observer<Result<*>> {
        if (it is Result.Error && it.exception is InvalidArgsException) return@Observer
        else Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun initObservers() {
        super.initObservers()
        with(viewModel) {
            addCityUseCaseLiveData.observe(viewLifecycleOwner) {
                popBackIfSuccesed(it)
            }
            addCityByLocationUseCaseLiveData.observe(viewLifecycleOwner) {
                popBackIfSuccesed(it)
            }
        }
    }

    private fun popBackIfSuccesed(result: Result<Int>) {
        if (result.succeeded) findNavController().popBackStack()
    }

    private fun initInputField() {
        et_add_city_find_by_name.addTextChangedListener {
            viewModel.search(it.toString())
        }
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

    private fun initRecycler() {
        with(recycler_add_city_results) {
            adapter = RvRemoteCitiesAdapter {
                viewModel.addCity(it)
            }
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
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