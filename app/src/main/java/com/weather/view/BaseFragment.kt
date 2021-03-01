package com.weather.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.data.common.Result
import com.weather.common.components.initBaseObservers
import com.weather.common.entities.DialogAlertType
import com.weather.viewmodel.BaseViewModel
import com.weather.viewmodel.MainViewModel
import com.weather.viewmodel.ViewModelFactory

abstract class BaseFragment : Fragment() {
    /**
     * Переопредели это значение, если текущий Destination не является конечной точкой навигации.
     * В таком случае, будет реализована подписка на localCities в sharedModel.
     * Это позволит реагировать на состояние списка, и, если он пуст - будет навигация к след. компоненту
     * */
    open val navResId: Int? = null

    val sharedViewModel: MainViewModel by activityViewModels {
        ViewModelFactory("MainViewModel", requireActivity().application)
    }
    abstract val viewModel: BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }


    protected open fun initObservers() {
        viewLifecycleOwner.initBaseObservers(viewModel)
        viewModel.errorEvent.observe(viewLifecycleOwner, errorEventObserver)
    }

    protected open val errorEventObserver = Observer<Result.Error> {
        Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
    }

    protected fun showDialogFragment(alertType: DialogAlertType) {
        MyDialogFragment.newInstance(alertType).show(childFragmentManager, "dialog")
    }
}