package com.ponymaker.avatarcreator.maker.ui.success

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.lvt.ads.util.Admob
import com.ponymaker.avatarcreator.maker.R
import com.ponymaker.avatarcreator.maker.core.base.BaseActivity
import com.ponymaker.avatarcreator.maker.core.extensions.checkPermissions
import com.ponymaker.avatarcreator.maker.core.extensions.goToSettings
import com.ponymaker.avatarcreator.maker.core.extensions.gone
import com.ponymaker.avatarcreator.maker.core.extensions.handleBackLeftToRight
import com.ponymaker.avatarcreator.maker.core.extensions.invisible
import com.ponymaker.avatarcreator.maker.core.extensions.loadImage
import com.ponymaker.avatarcreator.maker.core.extensions.loadNativeCollabAds
import com.ponymaker.avatarcreator.maker.core.extensions.requestPermission
import com.ponymaker.avatarcreator.maker.core.extensions.select
import com.ponymaker.avatarcreator.maker.core.extensions.setImageActionBar
import com.ponymaker.avatarcreator.maker.core.extensions.setTextActionBar
import com.ponymaker.avatarcreator.maker.core.extensions.showInterAll
import com.ponymaker.avatarcreator.maker.core.extensions.startIntentRightToLeft
import com.ponymaker.avatarcreator.maker.core.extensions.startIntentWithClearTop
import com.ponymaker.avatarcreator.maker.core.extensions.strings
import com.ponymaker.avatarcreator.maker.core.extensions.tap
import com.ponymaker.avatarcreator.maker.core.extensions.visible
import com.ponymaker.avatarcreator.maker.core.helper.UnitHelper
import com.ponymaker.avatarcreator.maker.core.utils.key.IntentKey
import com.ponymaker.avatarcreator.maker.core.utils.key.RequestKey
import com.ponymaker.avatarcreator.maker.core.utils.key.ValueKey
import com.ponymaker.avatarcreator.maker.core.utils.state.HandleState
import com.ponymaker.avatarcreator.maker.databinding.ActivitySuccessBinding
import com.ponymaker.avatarcreator.maker.ui.home.HomeActivity
import com.ponymaker.avatarcreator.maker.ui.my_creation.MyCreationActivity
import com.ponymaker.avatarcreator.maker.ui.permission.PermissionViewModel
import kotlinx.coroutines.launch

class SuccessActivity : BaseActivity<ActivitySuccessBinding>() {
    private val viewModel: SuccessViewModel by viewModels()
    private val permissionViewModel: PermissionViewModel by viewModels()

    override fun setViewBinding(): ActivitySuccessBinding {
        return ActivitySuccessBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
        viewModel.setPath(intent.getStringExtra(IntentKey.INTENT_KEY) ?: "")
        setButtonBackgrounds()
    }

    private fun setButtonBackgrounds() {
        binding.includeLayoutBottom.apply {
            
            tvDownload.select()
            tvShare.select()

        }
    }

    override fun dataObservable() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.pathInternal.collect { path ->
                        if (path.isNotEmpty()) {
                            loadImage(this@SuccessActivity, path, binding.imvImage)
                        }
                    }
                }
            }
        }
    }

    private fun handleBack() {
        handleBackLeftToRight()
    }
    override fun viewListener() {
        binding.apply {
            actionBar.apply {
                btnActionBarNextRight.tap {
                    showInterAll {
                        startIntentWithClearTop(HomeActivity::class.java)
                    }
                }
                btnActionBarLeftText.tap {  handleBack()  }

                btnActionBarRight.tap(2000){
                    viewModel.shareFiles(this@SuccessActivity)
                }
            }

            // My Album button
            includeLayoutBottom.btnWhatsapp.tap(2590) {
                showInterAll {
                    startIntentRightToLeft(MyCreationActivity::class.java, IntentKey.TAB_KEY, ValueKey.MY_DESIGN_TYPE)
                }
            }

            // Download button
            includeLayoutBottom.btnTelegram.tap(2000) {
                checkStoragePermission()
            }

        }
    }

    override fun initActionBar() {
        binding.actionBar.apply {

            btnActionBarLeftText.visible()
            btnActionBarRight.visible()
            btnActionBarRight.setBackgroundResource(R.drawable.ic_share)
            tvCenter.invisible()
            imgCenter.gone()
                setImageActionBar(btnActionBarNextRight, R.drawable.ic_home)
            btnActionBarNextRight.visible()

        }
    }

    private fun checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            handleDownload()
        } else {
            val perms = permissionViewModel.getStoragePermissions()
            if (checkPermissions(perms)) {
                handleDownload()
            } else if (permissionViewModel.needGoToSettings(sharePreference, true)) {
                goToSettings()
            } else {
                requestPermission(perms, RequestKey.STORAGE_PERMISSION_CODE)
            }
        }
    }

    private fun handleDownload() {
        lifecycleScope.launch {
            viewModel.downloadFiles(this@SuccessActivity).collect { state ->
                when (state) {
                    HandleState.LOADING -> showLoading()
                    HandleState.SUCCESS -> {
                        dismissLoading()
                        showToast(R.string.download_success)
                    }
                    else -> {
                        dismissLoading()
                        showToast(R.string.download_failed_please_try_again_later)
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == RequestKey.STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                permissionViewModel.updateStorageGranted(sharePreference, true)
                handleDownload()
            } else {
                permissionViewModel.updateStorageGranted(sharePreference, false)
            }
        }
    }

    override fun initAds() {
        initNativeCollab()
    }

    fun initNativeCollab() {

        loadNativeCollabAds(R.string.native_cl_ss, binding.flNativeCollab)


    }

    @android.annotation.SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        handleBackLeftToRight()
    }
}
