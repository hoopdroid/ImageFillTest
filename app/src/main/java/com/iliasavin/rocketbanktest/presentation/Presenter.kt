package com.iliasavin.rocketbanktest.presentation


abstract class BasePresenter<View> {
    protected var view: View? = null

    open fun onAttach(view: View) {
        this.view = view
    }

    open fun onDestroy() {
        this.view = null
    }
}