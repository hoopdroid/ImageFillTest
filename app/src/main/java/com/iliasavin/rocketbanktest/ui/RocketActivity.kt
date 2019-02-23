package com.iliasavin.rocketbanktest.ui

import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.iliasavin.rocketbanktest.R
import com.iliasavin.rocketbanktest.data.model.PixelColorState
import com.iliasavin.rocketbanktest.presentation.RocketPresenter
import kotlinx.android.synthetic.main.activity_rocket_image.*

class RocketActivity : AppCompatActivity(), RocketView {
    private val presenter: RocketPresenter by lazy { RocketPresenter() }

    override fun onStart() {
        super.onStart()

        presenter.onAttach(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rocket_image)

        generateButton.setOnClickListener {
            presenter.updateImage()
        }

        firstRocketImage.onPixelTouch = {
            presenter.fill(it)
        }

        secondRocketImage.onPixelTouch = {
            presenter.fill(it)
        }

        presenter.initImages(firstRocketImage.pixelImage)
    }

    override fun invalidate(pixelImage: Point, pixelColorState: PixelColorState) {
        Thread(Runnable { runOnUiThread { firstRocketImage.updateImage(pixelImage, pixelColorState) } }).start()
    }

    override fun regenerate() {
        firstRocketImage.generatePixels(64, 64)
        secondRocketImage.pixelImage = firstRocketImage.pixelImage
        secondRocketImage.invalidate()
    }
}
