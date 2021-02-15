package com.weather.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.weather.R
import com.weather.components.DialogAlertType
import kotlinx.android.synthetic.main.fragment_blank.*

private const val ALERT_TYPE = "ALERT_TYPE"

class MyDialogFragment : DialogFragment() {

    private val alertType by lazy {
        val ordinal = requireArguments().getInt(ALERT_TYPE)
        DialogAlertType.values()[ordinal]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        val (description, action) = buildComponents()
        text_dialog_description.text = description
        but_dialog_action.setOnClickListener(action)
        but_dialog_cancel.setOnClickListener { dismiss() }
    }

    private fun buildComponents(): Pair<String, (View) -> Unit> = when (alertType) {
        DialogAlertType.ALLOW_LOCATION_PERMISSION -> Pair("Включите службы определения местоположения") {
            with(Intent()) {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", requireActivity().packageName, null)
                this@MyDialogFragment.startActivity(this)
                dismiss()
            }
        }
        DialogAlertType.ENABLE_LOCATION -> Pair("Определение местоположения отключено. Включить?") {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            dismiss()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(alertType: DialogAlertType) =
            MyDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(ALERT_TYPE, alertType.ordinal)
                }
            }
    }
}