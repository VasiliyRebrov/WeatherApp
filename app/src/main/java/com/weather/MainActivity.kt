package com.weather

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.domain.Result
import com.domain.succeeded
import com.domain.successOr
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

class MainActivity : AppCompatActivity() {
    private val viewModel: AddCityViewModel by viewModels {
        ViewModelFactory("AddCityViewModel", application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        viewModel.searchResultLiveData.observe(this) {
            text_main_result.text = if (it is Result.Error) {
                it.exception.message
            } else
                it.toString()
        }

        et_main_example.addTextChangedListener {
            viewModel.search(it.toString())
        }
    }
}
