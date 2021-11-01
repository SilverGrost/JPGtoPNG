package ru.silcomsoft.jpgtopng.view

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatActivity
import ru.silcomsoft.jpgtopng.R
import ru.silcomsoft.jpgtopng.presenter.MainPresenter
import moxy.ktx.moxyPresenter
import ru.silcomsoft.jpgtopng.Constant.PERMISSIONS_READ
import ru.silcomsoft.jpgtopng.Constant.PERMISSIONS_WRITE
import ru.silcomsoft.jpgtopng.databinding.ActivityMainBinding
import ru.silcomsoft.jpgtopng.model.ImageConverterFactory
import ru.silcomsoft.jpgtopng.model.scheduler.SchedulerFactory


class MainActivity : MvpAppCompatActivity(R.layout.activity_main), IMainView {

    private lateinit var binding: ActivityMainBinding

    private val presenter: MainPresenter by moxyPresenter {
        MainPresenter(
            imageConverter = ImageConverterFactory.create(this),
            schedulers = SchedulerFactory.create()
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    override fun showMessage(message: String) {
        Snackbar.make(
            binding.mainActivity,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    override fun showResultImage(uri: Uri?) {
        Glide.with(this)
            .load(uri)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .error(R.drawable.ic_img_def)
            .into(binding.completeImg)
    }

    override fun useConvertButtons(use: Boolean) {
        when (use) {
            true -> {
                binding.btnConvert.isEnabled = true
                binding.btnCancel.isEnabled = true
            }
            else -> {
                binding.btnConvert.isEnabled = false
                binding.btnCancel.isEnabled = false
            }
        }
    }

    override fun showSelectedImage(uri: Uri?) {
        Glide.with(this)
            .load(uri)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .error(R.drawable.ic_img_def)
            .into(binding.targetImg)
    }


    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                this.contentResolver.openInputStream(uri)?.readBytes()?.let { _ ->
                    presenter.onImageSelected(it)
                }
            }
        }

    override fun chooseImage() {
        imagePicker.launch("image/*")
    }


    override fun showLoading(isLoading: Boolean) {
        if (isLoading)
            binding.progressBar.visibility = View.VISIBLE
        else
            binding.progressBar.visibility = View.GONE
    }

    private val permissionRequestReadFile =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                presenter.selectImage()
            } else {
                showMessage(getString(R.string.permission_failed_to_read_files))
            }
        }

    private val permissionRequestWriteFile =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                presenter.convertStart(this)
            } else {
                showMessage(getString(R.string.permission_failed_to_write_files))
            }
        }

    private fun checkReadFilesPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                PERMISSIONS_READ
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            presenter.selectImage()
        } else {
            if (shouldShowRequestPermissionRationale(PERMISSIONS_READ)) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.need_permission_read_file),
                    Snackbar.LENGTH_LONG
                ).setAction(getString(R.string.grant))
                {
                    permissionRequestReadFile.launch(PERMISSIONS_READ)
                }.show()
            } else {
                permissionRequestReadFile.launch(PERMISSIONS_READ)
            }
        }
    }

    private fun setImage() {
        checkReadFilesPermission()
    }

    private fun checkWriteFilesPermission() {
        /*if (ContextCompat.checkSelfPermission(
                this,
                PERMISSIONS_WRITE
            ) == PackageManager.PERMISSION_GRANTED
        ) {*/
            presenter.convertStart(this)
        /*} else {
            if (shouldShowRequestPermissionRationale(PERMISSIONS_WRITE)) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.need_permission_write_file),
                    Snackbar.LENGTH_LONG
                ).setAction(getString(R.string.grant))
                {
                    permissionRequestWriteFile.launch(PERMISSIONS_WRITE)
                }.show()
            } else {
                permissionRequestWriteFile.launch(PERMISSIONS_WRITE)
            }
        }*/
    }

    private fun convertStart() {
        checkWriteFilesPermission()
    }

    private fun convertCancel() {
        presenter.convertStop()
        showLoading(false)
    }

    private fun init() {
        with(binding) {
            button.setOnClickListener { setImage() }
            btnConvert.setOnClickListener { convertStart() }
            btnCancel.setOnClickListener { convertCancel() }
        }
        showLoading(false)
        useConvertButtons(false)
    }

}