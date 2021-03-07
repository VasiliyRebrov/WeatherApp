package com.weather.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.data.common.Result
import com.data.model.City
import com.weather.common.components.initBaseObservers
import com.weather.common.entities.DialogType
import com.weather.viewmodel.BaseViewModel
import com.weather.viewmodel.MainViewModel
import com.weather.viewmodel.ViewModelFactory

abstract class BaseFragment : Fragment() {
    protected var _binding: ViewDataBinding? = null

    /**исп. между [onCreateView] & [onDestroyView]*/
    protected val binding get() = _binding!!

    val sharedModel: MainViewModel by activityViewModels {
        ViewModelFactory(MainActivity::class.java.simpleName, requireActivity().application)
    }

    /** Сделай приведение в субклассе к нужному типу субкласса [BaseViewModel]*/
    open val model: BaseViewModel by viewModels {
        ViewModelFactory(this::class.java.simpleName, requireActivity().application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflate(inflater, container, savedInstanceState)


    abstract fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    /**
     * Если нужно добавить наблюдателей - переопредели этот метод и внеси их.
     * Переопределяя, необходимо сохранить вызов базовой реализации*/
    protected open fun initObservers() {
        viewLifecycleOwner.initBaseObservers(model)
        sharedModel.localCitiesByIdLD.observe(viewLifecycleOwner, localCitiesObserver)
        model.errorEvent.observe(viewLifecycleOwner, errorEventObserver)
    }

    /** [localCitiesObserver] - базовая реализация надлюдателя локальных городов.
     * Переопредели, если необходимо реагировать на изменения списка*/
    open val localCitiesObserver: (Result<List<City>>) -> Unit = {}

    /**
     * [errorEventObserver] - базовая реализация обработки событий ошибок.
     * По умолчанию каждая ошибка вызывает [Toast].
     * Переопредели, конкретизируя конкретный кейс*/
    protected open val errorEventObserver = Observer<Result.Error> {
        Toast.makeText(requireContext(), it.exception.cause!!.message, Toast.LENGTH_SHORT).show()
    }

    protected fun showDialogFragment(type: DialogType) {
        MyDialogFragment.newInstance(type).show(childFragmentManager, "dialog")
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}