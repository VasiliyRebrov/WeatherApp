package com.weather.view

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.data.common.Result
import com.weather.R
import com.weather.components.DialogAlertType
import com.weather.viewmodel.BaseViewModel
import com.weather.viewmodel.MainViewModel
import com.weather.viewmodel.ViewModelFactory
import kotlinx.coroutines.runBlocking

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