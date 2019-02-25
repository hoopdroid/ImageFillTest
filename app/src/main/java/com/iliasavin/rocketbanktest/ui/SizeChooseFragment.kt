package com.iliasavin.rocketbanktest.ui

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.iliasavin.rocketbanktest.R
import kotlinx.android.synthetic.main.fragment_size.*

const val MIN_SIZE_VALUE = 4
const val MAX_SIZE_VALUE = 64

class SizeChooseFragment : DialogFragment() {
    private val clickListener = View.OnClickListener { view ->
        when (view.id) {
            buttonCancel.id -> dismiss()
            buttonOk.id -> {
                val activity = activity as SizeChooseListener
                val rows = editRows.text.toString()
                val cols = editColumns.text.toString()
                if (rows.isNotEmpty() && cols.isNotEmpty() &&
                    rows == cols && rows.toInt() >= MIN_SIZE_VALUE && rows.toInt() <= MAX_SIZE_VALUE
                ) {
                    activity.updateResult(editRows.text.toString().toInt(), editColumns.text.toString().toInt())
                    dismiss()
                } else {
                    Toast.makeText(
                        getActivity(),
                        getString(R.string.size_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.iliasavin.rocketbanktest.R.layout.fragment_size, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrayOf(buttonCancel, buttonOk).forEach { it.setOnClickListener(clickListener) }
    }

    companion object {
        val SIZE_CHOOOSE_FRAGMENT_TAG = "Change Size"
    }
}