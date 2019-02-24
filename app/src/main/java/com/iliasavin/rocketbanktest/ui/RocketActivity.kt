package com.iliasavin.rocketbanktest.ui

import android.R
import android.graphics.Point
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.iliasavin.rocketbanktest.data.model.PixelColorState
import com.iliasavin.rocketbanktest.data.model.PixelImage
import com.iliasavin.rocketbanktest.presentation.BasePresenter
import com.iliasavin.rocketbanktest.presentation.RocketPresenter
import com.iliasavin.rocketbanktest.util.onChange
import com.iliasavin.rocketbanktest.util.onSelection
import kotlinx.android.synthetic.main.activity_rocket_image.*

class RocketActivity : BaseActivity(), RocketView, SizeChooseListener {
    private lateinit var presenter: RocketPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.iliasavin.rocketbanktest.R.layout.activity_rocket_image)

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

    override fun regenerate(image: PixelImage) {
        firstRocketImage.updateImage(image)
        secondRocketImage.updateImage(image)
    }

    fun initViewElements() {
        generateButton.setOnClickListener {
            presenter.clearAndStop()

            presenter.initImages(presenter.rowSize, presenter.columnSize)
        }
        firstRocketImage.onPixelTouch = { presenter.fill(it) }
        secondRocketImage.onPixelTouch = { presenter.fill(it) }
        changeSizeButton?.setOnClickListener {
            val transaction = supportFragmentManager
                .beginTransaction()
            SizeChooseFragment().show(transaction, "Change Size")
        }

        seekBarSpeed.onChange {
            presenter.changeSpeed((100 - it) * 10L)
        }

        setSpinner()

    }

    private fun setSpinner() {
        val adapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item,
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
        presenter.initImages(sizeWidth, sizeHeight)
    }
}

interface SizeChooseListener {
    fun updateResult(sizeWidth: Int, sizeHeight: Int)
}

