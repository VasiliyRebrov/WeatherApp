package com.weather.common.components

import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.weather.viewmodel.BaseViewModel
import kotlinx.android.synthetic.main.fragment_general.*
import kotlinx.coroutines.suspendCancellableCoroutine

fun LifecycleOwner.initBaseObservers(model: BaseViewModel) {
    fun <T> LiveData<T>.observeWithLogging(
        name: String,
        observer: ((T) -> Unit)? = null
    ) {
        val loggingObserver: (T) -> Unit = {
            Log.d("UseCaseResult", "\nusecase: $name\nresult: ${it.toString()}\n")
            observer?.invoke(it)
        }
        this.observe(this@initBaseObservers, loggingObserver)
    }
    with(model) {
        useCases.forEach { it.value.observeWithLogging(it.key) }
    }
}

suspend fun View.awaitLayoutChange(action: () -> Unit) = suspendCancellableCoroutine<Unit> { cont ->
    val listener = object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            view: View?,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int
        ) {
            view?.removeOnLayoutChangeListener(this)
            action()
            cont.resume(Unit, null)
        }
    }
    addOnLayoutChangeListener(listener)
    cont.invokeOnCancellation { removeOnLayoutChangeListener(listener) }
}

