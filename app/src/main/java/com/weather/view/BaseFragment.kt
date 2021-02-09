package com.weather.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.weather.viewmodel.MainViewModel
import com.weather.viewmodel.ViewModelFactory

abstract class BaseFragment : Fragment() {
    val sharedViewModel: MainViewModel by activityViewModels {
        ViewModelFactory("MainViewModel", requireActivity().application)
    }

//    protected fun showDialogFragment(alertType: DialogAlertType) {
//        MyDialogFragment.newInstance(alertType).show(childFragmentManager, "dialog")
//    }

}