package com.iliasavin.rocketbanktest.ui

import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.iliasavin.rocketbanktest.R
import com.iliasavin.rocketbanktest.core.extension.onChange
import com.iliasavin.rocketbanktest.core.extension.onSelection
import com.iliasavin.rocketbanktest.data.fill.FillMethods
import com.iliasavin.rocketbanktest.data.model.PixelColorState
import com.iliasavin.rocketbanktest.data.model.PixelImage
import com.iliasavin.rocketbanktest.presentation.BasePresenter
import com.iliasavin.rocketbanktest.presentation.RocketPresenter
import kotlinx.android.synthetic.main.activity_rocket_image.*

class RocketActivity : BaseActivity(), RocketView, SizeChooseListener {
    private lateinit var presenter: RocketPresenter
    private val sizeChooseFragment by lazy { SizeChooseFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rocket_image)

        presenter =
            if (lastCustomNonConfigurationInstance == null) {
                RocketPresenter()
            } else {
                lastCustomNonConfigurationInstance as RocketPresenter
            }
        presenter.onAttach(this)
        configureUI()
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.onDestroy()
    }

    override fun refreshFirstImage(pixel: Point, stateColor: PixelColorState) {
        firstRocketImage.updatePixel(pixel, stateColor)
    }

    override fun refreshSecondImage(pixel: Point, stateColor: PixelColorState) {
        secondRocketImage.updatePixel(pixel, stateColor)
    }

    override fun showTimeResult(result: String) {
        Log.d(this.javaClass.simpleName, result)
    }

    override fun update(pixelImage: PixelImage) {
        firstRocketImage.updateImage(pixelImage)
        secondRocketImage.updateImage(pixelImage)
    }

    private val clickListener = View.OnClickListener { view ->
        when (view.id) {
            generateButton.id -> {
                // if change size button null -> landscape
                if (changeSizeButton == null) {
                    val rows = editRows.text.toString()
                    val cols = editColumns.text.toString()
                    if (rows.isNotEmpty() && cols.isNotEmpty() &&
                        rows == cols && rows.toInt() >= MIN_SIZE_VALUE && rows.toInt() <= MAX_SIZE_VALUE
                    ) {
                        updateResult(rows.toInt(), cols.toInt())
                    } else {
                        Toast.makeText(
                            this,
                            getString(R.string.size_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    updateResult(presenter.rowSize, presenter.columnSize)
                }
            }
        }
    }

    fun configureUI() {
        arrayOf(generateButton).forEach { it.setOnClickListener(clickListener) }
        firstRocketImage.onPixelTouch = { presenter.fill(it) }
        secondRocketImage.onPixelTouch = { presenter.fill(it) }
        seekBarSpeed.onChange { presenter.changeSpeed((100 - it) * 10L) }
        setSpinner()

        // non existing in landscape
        changeSizeButton?.setOnClickListener {
            sizeChooseFragment.show(
                supportFragmentManager,
                SizeChooseFragment.SIZE_CHOOOSE_FRAGMENT_TAG
            )
        }
    }

    private fun setSpinner() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listOf("BFS", "DFS", "Dummy")
        )

        firstMethodSpinner.adapter = adapter
        secondMethodSpinner.adapter = adapter

        firstMethodSpinner.setSelection(0)
        secondMethodSpinner.setSelection(1)
        firstMethodSpinner.onSelection {
            selectFirstFillMethod(it)
        }
        secondMethodSpinner.onSelection {
            selectSecondFillMethod(it)
        }
    }

    private fun selectFirstFillMethod(it: Int) {
        when (it) {
            FillMethods.BFS.id -> presenter.setImageFillMethod(FillMethods.BFS.id, true)
            FillMethods.DFS.id -> presenter.setImageFillMethod(FillMethods.DFS.id, true)
            FillMethods.DUMMY.id -> presenter.setImageFillMethod(FillMethods.DUMMY.id, true)
        }
    }

    private fun selectSecondFillMethod(it: Int) {
        when (it) {
            FillMethods.BFS.id -> presenter.setImageFillMethod(FillMethods.BFS.id, false)
            FillMethods.DFS.id -> presenter.setImageFillMethod(FillMethods.DFS.id, false)
            FillMethods.DUMMY.id -> presenter.setImageFillMethod(FillMethods.DUMMY.id, false)
        }
    }

    override fun onRetainCustomNonConfigurationInstance(): BasePresenter<RocketView> {
        return presenter
    }

    override fun updateResult(sizeWidth: Int, sizeHeight: Int) {
        presenter.clearAndStop()
        presenter.updateImages(sizeWidth, sizeHeight)
    }
}

interface SizeChooseListener {
    fun updateResult(sizeWidth: Int, sizeHeight: Int)
}

