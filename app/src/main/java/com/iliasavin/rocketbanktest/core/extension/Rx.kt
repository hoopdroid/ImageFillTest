package com.iliasavin.rocketbanktest.core.extension

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Observable<T>.applyIoScheduler() = applyScheduler(Schedulers.io())
fun <T> Observable<T>.applyComputationScheduler() = applyScheduler(Schedulers.computation())
private fun <T> Observable<T>.applyScheduler(scheduler: Scheduler) =
    subscribeOn(scheduler).observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.applyIOScheduler() = applyMainThreadScheduler(Schedulers.io())
fun <T> Single<T>.applyComputationScheduler() = applyComputationThreadScheduler(Schedulers.computation())

private fun <T> Single<T>.applyMainThreadScheduler(scheduler: Scheduler) =
    subscribeOn(scheduler).observeOn(AndroidSchedulers.mainThread())

private fun <T> Single<T>.applyComputationThreadScheduler(scheduler: Scheduler) =
    subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(Schedulers.computation())