package com.weather.view

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import com.weather.viewmodel.MainViewModel
import com.weather.viewmodel.ViewModelFactory

abstract class BaseFragment : Fragment() {
    val sharedViewModel: MainViewModel by activityViewModels {
        ViewModelFactory("MainViewModel", requireActivity().application)
    }

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
}