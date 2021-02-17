package com.weather.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.data.common.Result
import com.weather.R
import com.weather.components.DialogAlertType
import com.weather.viewmodel.BaseViewModel
import com.weather.viewmodel.MainViewModel
import com.weather.viewmodel.ViewModelFactory
import kotlinx.coroutines.runBlocking

abstract class BaseFragment : Fragment() {
    val sharedViewModel: MainViewModel by activityViewModels {
        ViewModelFactory("MainViewModel", requireActivity().application)
    }
    abstract val viewModel: BaseViewModel

    protected fun showDialogFragment(alertType: DialogAlertType) {
        MyDialogFragment.newInstance(alertType).show(childFragmentManager, "dialog")
    }

    protected fun <T> LiveData<T>.observeWithLogging(observer: ((T) -> Unit)? = null) {
        val loggingObserver: (T) -> Unit = {
            Log.d("MyTag", it.toString())
            observer?.invoke(it)
        }
        this.observe(viewLifecycleOwner, loggingObserver)
    }

    fun isExistCities(navResId: Int) {
        runBlocking {
            if (!sharedViewModel.isExistCities())
                findNavController().navigate(navResId)
            else
                println()
        }
    }
}