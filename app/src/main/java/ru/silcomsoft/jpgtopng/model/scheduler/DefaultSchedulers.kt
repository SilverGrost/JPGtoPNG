package ru.silcomsoft.jpgtopng.model.scheduler

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

class DefaultSchedulers : Schedulers {

    override fun background(): Scheduler = io.reactivex.schedulers.Schedulers.newThread()
    override fun computation(): Scheduler = io.reactivex.schedulers.Schedulers.computation()
    override fun main(): Scheduler = AndroidSchedulers.mainThread()
}