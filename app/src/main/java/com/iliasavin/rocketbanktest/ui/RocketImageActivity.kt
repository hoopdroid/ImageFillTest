package com.iliasavin.rocketbanktest.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.iliasavin.rocketbanktest.R
import kotlinx.android.synthetic.main.activity_rocket_image.*

class RocketImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rocket_image)

        generateButton.setOnClickListener {
            firstRocketImage.generatePixels(16, 16)
            secondRocketImage.generatePixels(16, 16)
        }


    }
}
