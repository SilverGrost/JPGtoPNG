package ru.silcomsoft.jpgtopng.model

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import io.reactivex.Single
import ru.silcomsoft.jpgtopng.Constant.DEF_DELAY
import ru.silcomsoft.jpgtopng.Constant.DEF_QUALITY
import ru.silcomsoft.jpgtopng.Constant.ERROR_CONVERT
import java.io.File


class ImageConverterImpl(private val context: Context) : IImageConverter {

    override fun jpegToPng(uriTargetImage: Uri, toFile: File): Single<Uri> =
        Single.create { emitter ->
            try {
                Thread.sleep(DEF_DELAY)
                val bitmap =
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uriTargetImage)
                val result =
                    bitmap.compress(Bitmap.CompressFormat.PNG, DEF_QUALITY, toFile.outputStream())
                if (!emitter.isDisposed) {
                    if (result) {
                        emitter.onSuccess(Uri.fromFile(toFile))
                    } else {
                        emitter.onError(Exception(ERROR_CONVERT))
                    }
                }
            } catch (e: Throwable) {
                if (!emitter.isDisposed) {
                    emitter.onError(e)
                }
            }
        }
}