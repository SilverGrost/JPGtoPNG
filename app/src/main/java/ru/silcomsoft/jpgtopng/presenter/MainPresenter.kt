package ru.silcomsoft.jpgtopng.presenter


import android.content.Context
import android.net.Uri
import android.os.Environment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import moxy.MvpPresenter
import ru.silcomsoft.jpgtopng.Constant.CANCEL_CONVERT
import ru.silcomsoft.jpgtopng.Constant.DEF_FILE_NAME
import ru.silcomsoft.jpgtopng.Constant.IMAGE_NOT_SELECTED
import ru.silcomsoft.jpgtopng.Constant.SUCCESS_CONVERT
import ru.silcomsoft.jpgtopng.view.IMainView
import ru.silcomsoft.jpgtopng.model.IImageConverter
import ru.silcomsoft.jpgtopng.model.scheduler.Schedulers
import java.io.File

class MainPresenter(
    private val imageConverter: IImageConverter,
    private val schedulers: Schedulers,
) : MvpPresenter<IMainView>() {

    private var disposables = CompositeDisposable()
    private var uriSelected: Uri? = null


    fun selectImage() {
        viewState.chooseImage()
    }

    fun convertStart(context: Context) {
        viewState.showLoading(true)
        //val toSaveFile = File(Environment.getExternalStorageDirectory().toString(), DEF_FILE_NAME)
        val toSaveFile = File(context.cacheDir, DEF_FILE_NAME)

        when (uriSelected) {
            null -> {
                viewState.showLoading(false)
                viewState.showMessage(IMAGE_NOT_SELECTED)
            }
            else -> {
                uriSelected?.let { uri ->
                    imageConverter
                        .jpegToPng(uri, toSaveFile)
                        .observeOn(schedulers.main())
                        .subscribeOn(schedulers.computation())
                        .subscribe(
                            {
                                with(viewState) {
                                    showResultImage(it)
                                    showLoading(false)
                                    showMessage(SUCCESS_CONVERT)
                                }
                            },
                            {
                                with(viewState) {
                                    showLoading(false)
                                    showMessage(it.message.toString())
                                    useConvertButtons(false)
                                    showSelectedImage(null)
                                    showResultImage(null)
                                }
                                uriSelected = null
                            }
                        ).addTo(disposables)
                }
            }
        }
    }

    fun convertStop() {
        with(viewState) {
            showSelectedImage(null)
            showResultImage(null)
            useConvertButtons(false)
            showLoading(false)
            showMessage(CANCEL_CONVERT)
        }
        uriSelected = null
        disposables.clear()
    }

    fun onImageSelected(targetUri: Uri) {
        uriSelected = targetUri
        viewState.showSelectedImage(targetUri)
        viewState.useConvertButtons(true)
    }

}