package com.weather.components

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import com.weather.viewmodel.BaseViewModel

fun LifecycleOwner.initBaseObservers(viewModel: BaseViewModel) {
    fun <T> LiveData<T>.observeWithLogging(
        name: String,
        observer: ((T) -> Unit)? = null
    ) {
        val loggingObserver: (T) -> Unit = {
            Log.d("UseCaseResult", "usecase: $name | result ${it.toString()}")
            observer?.invoke(it)
        }
        this.observe(this@initBaseObservers, loggingObserver)
    }
    with(viewModel) {
        liveDataContainer.forEach { it.value.observeWithLogging(it.key) }
//        baseLiveData.observeWithLogging("base")
//        usecaseEvent.observeWithLogging("event") {
//            Toast.makeText(ctx, it.toString(), Toast.LENGTH_SHORT).show()
//        }
    }
}

