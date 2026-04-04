package com.ponymaker.avatarcreator.maker.dialog

import android.app.Activity
import android.graphics.Color
import com.lvt.ads.util.Admob
import com.ponymaker.avatarcreator.maker.core.extensions.gone
import com.ponymaker.avatarcreator.maker.core.extensions.hideNavigation
import com.ponymaker.avatarcreator.maker.core.extensions.tap
import com.ponymaker.avatarcreator.maker.R
import com.ponymaker.avatarcreator.maker.core.base.BaseDialog
import com.ponymaker.avatarcreator.maker.core.extensions.strings
import com.ponymaker.avatarcreator.maker.core.extensions.visible
import com.ponymaker.avatarcreator.maker.databinding.DialogConfirmBinding

class YesNoDialog(
    val context: Activity,
    val title: Int,
    val description: Int,
    val isError: Boolean = false,
    val showNativeAd: Boolean = false
) : BaseDialog<DialogConfirmBinding>(context, maxWidth = true, maxHeight = true) {
    override val layoutId: Int = R.layout.dialog_confirm
    override val isCancelOnTouchOutside: Boolean = false
    override val isCancelableByBack: Boolean = false

    var onNoClick: (() -> Unit) = {}
    var onYesClick: (() -> Unit) = {}
    var onDismissClick: (() -> Unit) = {}

    override fun initView() {
        initText()
        initBackground()
        if (isError) {
            binding.btnNo.gone()
        }
        context.hideNavigation()
        binding.tvTitle.isSelected = true
        if (showNativeAd) {
            binding.flNativeAd.visible()
            Admob.getInstance().loadNativeAd(
                context,
                context.getString(R.string.native_dialog),
                binding.flNativeAd,
                com.lvt.ads.R.layout.ads_native_avg2
            )
        }
    }

    private fun initBackground() {
        binding.containerDialog.setBackgroundResource(R.drawable.bg_body_dialog)
        binding.tvDescription.setTextColor(Color.parseColor("#4D4D4D"))
        binding.btnNo.setBackgroundResource(R.drawable.bg_btn_permission_no)
        binding.btnYes.setBackgroundResource(R.drawable.bg_btn_permission_yes)
        binding.btnYes.setTextColor(Color.parseColor("#FFFFFF"))
        val paddingVertical = (9 * context.resources.displayMetrics.density).toInt()
        binding.btnNo.setPadding(0, paddingVertical, 0, paddingVertical)
        binding.btnYes.setPadding(0, paddingVertical, 0, paddingVertical)
    }

    override fun initAction() {
        binding.apply {
            btnNo.tap { onNoClick.invoke() }
            btnYes.tap { onYesClick.invoke() }
            flOutSide.tap { onDismissClick.invoke() }
        }
    }

    override fun onDismissListener() {}

    private fun initText() {
        binding.apply {
            tvTitle.text = context.strings(title)
            tvDescription.text = context.strings(description)
            if (isError) {
                btnYes.text = context.strings(R.string.ok)
            }
        }
    }
}
