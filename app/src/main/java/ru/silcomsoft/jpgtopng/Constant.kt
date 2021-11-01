package ru.silcomsoft.jpgtopng

import android.Manifest

object Constant {
    const val PERMISSIONS_READ = Manifest.permission.READ_EXTERNAL_STORAGE
    const val PERMISSIONS_WRITE = Manifest.permission.WRITE_EXTERNAL_STORAGE
    const val DEF_FILE_NAME = "saved.png"
    const val IMAGE_NOT_SELECTED = "Не выбран рисунок"
    const val SUCCESS_CONVERT = "Изображение успешно сконвертировано в PNG формат"
    const val CANCEL_CONVERT = "Отмена конвертирования"
    const val DEF_QUALITY = 100
    const val DEF_DELAY = 3000L
    const val ERROR_CONVERT = "При конвертировании произошла ошибка."
}