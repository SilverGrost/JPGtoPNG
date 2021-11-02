package ru.silcomsoft.jpgtopng.view

import android.net.Uri
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface IMainView: MvpView {
    fun chooseImage()
    fun showLoading(isLoading: Boolean)
    fun showMessage(message: String)
    fun showResultImage(uri: Uri?)
    fun useConvertButtons(use: Boolean)
    fun showSelectedImage(uri: Uri?)
}