package com.iliasavin.rocketbanktest.ui

import android.support.v7.app.AppCompatActivity
import com.iliasavin.rocketbanktest.presentation.BasePresenter

abstract class BaseActivity : AppCompatActivity() {
    abstract override fun onRetainCustomNonConfigurationInstance(): BasePresenter<RocketView>
}