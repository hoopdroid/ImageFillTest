package com.iliasavin.rocketbanktest.ui

import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.iliasavin.rocketbanktest.R
import com.iliasavin.rocketbanktest.core.extension.onChange
import com.iliasavin.rocketbanktest.core.extension.onSelection
import com.iliasavin.rocketbanktest.data.model.PixelColorState
import com.iliasavin.rocketbanktest.data.model.PixelImage
import com.iliasavin.rocketbanktest.presentation.BasePresenter
import com.iliasavin.rocketbanktest.presentation.RocketPresenter
import kotlinx.android.synthetic.main.activity_rocket_image.*

class RocketActivity : BaseActivity(), RocketView, SizeChooseListener {
    private lateinit var presenter: RocketPresenter
    private val sizeChooseFragment by lazy { SizeChooseFragment() }
    private val clickListener = View.OnClickListener { view ->
        when (view.id) {
            generateButton.id -> {
                updateResult(presenter.rowSize, presenter.columnSize)
            }
            changeSizeButton.id -> {
                sizeChooseFragment.show(supportFragmentManager, SizeChooseFragment.SIZE_CHOOOSE_FRAGMENT_TAG)
            }
        }
    }

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
        initViewElements()
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
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
    }

    override fun update(image: PixelImage) {
        firstRocketImage.updateImage(image)
        secondRocketImage.updateImage(image)
    }

    fun initViewElements() {
        arrayOf(generateButton, changeSizeButton).forEach { it.setOnClickListener(clickListener) }
        firstRocketImage.onPixelTouch = { presenter.fill(it) }
        secondRocketImage.onPixelTouch = { presenter.fill(it) }
        seekBarSpeed.onChange { presenter.changeSpeed((100 - it) * 10L) }
        setSpinner()
    }

    private fun setSpinner() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf("BFS", "DFS")
        )

        firstMethodSpinner.adapter = adapter
        // TODO Add BFS/DFS selection
        firstMethodSpinner.onSelection { }
        firstMethodSpinner.setSelection(0)

        secondMethodSpinner.adapter = adapter
        secondMethodSpinner.onSelection { }
        secondMethodSpinner.setSelection(1)
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

