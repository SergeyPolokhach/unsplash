package com.polohach.unsplash.ui.screens.main.photo.detail

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cleveroad.bootstrap.kotlin_core.utils.misc.MiscellaneousUtils.getExtra
import com.cleveroad.bootstrap.kotlin_ext.getBitmap
import com.cleveroad.bootstrap.kotlin_ext.safeLet
import com.cleveroad.bootstrap.kotlin_ext.setClickListeners
import com.cleveroad.bootstrap.kotlin_permissionrequest.PermissionRequest
import com.cleveroad.bootstrap.kotlin_permissionrequest.PermissionResult
import com.polohach.unsplash.BuildConfig
import com.polohach.unsplash.R
import com.polohach.unsplash.enums.RequestCode
import com.polohach.unsplash.extensions.loadImageWithListener
import com.polohach.unsplash.extensions.showOrHide
import com.polohach.unsplash.ui.base.BaseFragment
import com.polohach.unsplash.utils.EMPTY_STRING
import com.polohach.unsplash.utils.IMAGE_MIME_TYPE
import kotlinx.android.synthetic.main.fragment_detail_photo.*
import java.io.File


class DetailPhotoFragment : BaseFragment<DetailPhotoVM>(), View.OnClickListener {

    companion object {
        private val EXTRA_DOWNLOAD_URL = getExtra("EXTRA_DOWNLOAD_URL", DetailPhotoFragment::class.java)
        private val EXTRA_PHOTO_URL = getExtra("EXTRA_PHOTO_URL", DetailPhotoFragment::class.java)
        private val EXTRA_PHOTO_ID = getExtra("EXTRA_PHOTO_ID", DetailPhotoFragment::class.java)

        fun newInstance(id: String, downloadUrl: String, photoUrl: String) =
                DetailPhotoFragment().apply {
                    arguments = Bundle().apply {
                        putString(EXTRA_DOWNLOAD_URL, downloadUrl)
                        putString(EXTRA_PHOTO_URL, photoUrl)
                        putString(EXTRA_PHOTO_ID, id)
                    }
                }
    }

    override val layoutId: Int = R.layout.fragment_detail_photo
    override val viewModelClass = DetailPhotoVM::class.java
    override val viewModel: DetailPhotoVM by lazy(LazyThreadSafetyMode.NONE) {
        activity
                ?.let { ViewModelProviders.of(it).get(viewModelClass) }
                ?: let { ViewModelProviders.of(this).get(viewModelClass) }
    }
    private val permissionRequest: PermissionRequest = PermissionRequest()
    private var downloadUrl: String = EMPTY_STRING
    private var photoUlr: String = EMPTY_STRING
    private var photoId: String = EMPTY_STRING
    private var photoFileTemp: File? = null

    override fun getScreenTitle() = R.string.title_detail_photo

    override fun getToolbarId() = R.id.toolbar

    override fun hasToolbar() = true

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        getExtra()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) =
            super.onViewCreated(view, savedInstanceState).also {
                setupUi()
                setClickListeners(ivPhoto, tvShare, tvDownload)
            }

    override fun observeLiveData(viewModel: DetailPhotoVM) {
        viewModel.apply {
            sharePhotoLD.observe(this@DetailPhotoFragment, Observer {
                it?.run {
                    sharePhoto(value)
                    sharePhotoLD.value = null
                }
            })
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionRequest.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onStart() {
        super.onStart()
        photoFileTemp?.delete()
        photoFileTemp = null
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ivPhoto -> llButtonContainer.showOrHide()
            R.id.tvShare -> checkPermission { share() }
            R.id.tvDownload -> checkPermission { download() }
        }
    }

    private fun getExtra() {
        arguments?.apply {
            downloadUrl = getString(EXTRA_DOWNLOAD_URL).orEmpty()
            photoUlr = getString(EXTRA_PHOTO_URL).orEmpty()
            photoId = getString(EXTRA_PHOTO_ID).orEmpty()
        }
    }

    private fun setupUi() {
        showProgress()
        ivPhoto.loadImageWithListener(photoUlr, { hideProgress() }, { hideProgress() })
    }

    private fun checkPermission(block: () -> Unit = {}) {
        permissionRequest.request(this,
                RequestCode.REQUEST_CREATE_TEMP_FILE(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                object : PermissionResult {
                    override fun onPermissionGranted() {
                        block()
                    }
                })
    }

    private fun share() {
        viewModel.createShareFile(context, ivPhoto.getBitmap())
    }

    private fun download() {
        viewModel.downloadPhoto(context, photoId)
    }

    private fun sharePhoto(photoFile: File?) {
        safeLet(photoFile, context) { file, ctx ->
            Intent(Intent.ACTION_SEND).apply {
                type = IMAGE_MIME_TYPE
                putExtra(Intent.EXTRA_STREAM,
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            FileProvider.getUriForFile(ctx, BuildConfig.FILEPROVIDER_NAME, file)
                        } else {
                            Uri.fromFile(file)
                        })
                startActivity(Intent.createChooser(this, getString(R.string.share_image_to)))
            }
            photoFileTemp = file
        }
    }
}
