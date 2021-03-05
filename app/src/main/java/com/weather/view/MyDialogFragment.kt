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
import com.weather.common.entities.DialogType
import kotlinx.android.synthetic.main.fragment_blank.*

private const val DIALOG_TYPE = "DIALOG_TYPE"

class MyDialogFragment : DialogFragment() {

    private val dialogType by lazy {
        val ordinal = requireArguments().getInt(DIALOG_TYPE)
        DialogType.values()[ordinal]
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

    private fun buildComponents(): Pair<String, (View) -> Unit> = when (dialogType) {
        DialogType.ALLOW_LOCATION_PERMISSION -> Pair("Включите службы определения местоположения") {
            with(Intent()) {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", requireActivity().packageName, null)
                this@MyDialogFragment.startActivity(this)
                dismiss()
            }
        }
        DialogType.ENABLE_LOCATION -> Pair("Определение местоположения отключено. Включить?") {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            dismiss()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(type: DialogType) =
            MyDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(DIALOG_TYPE, type.ordinal)
                }
            }
    }
}