package com.ponymaker.avatarcreator.maker.ui.add_character.adapter

import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ponymaker.avatarcreator.maker.core.base.BaseAdapter
import com.ponymaker.avatarcreator.maker.core.extensions.gone
import com.ponymaker.avatarcreator.maker.core.extensions.loadImage
import com.ponymaker.avatarcreator.maker.core.extensions.select
import com.ponymaker.avatarcreator.maker.core.extensions.tap
import com.ponymaker.avatarcreator.maker.core.extensions.visible
import com.ponymaker.avatarcreator.maker.data.model.SelectedModel
import com.ponymaker.avatarcreator.maker.databinding.ItemBackgroundImageBinding

class BackgroundImageAdapter :
    BaseAdapter<SelectedModel, ItemBackgroundImageBinding>(ItemBackgroundImageBinding::inflate) {
    var onAddImageClick: (() -> Unit) = {}
    var onBackgroundImageClick: ((String, Int) -> Unit) = {_,_ ->}
    var currentSelected = -1

    override fun onBind(binding: ItemBackgroundImageBinding, item: SelectedModel, position: Int) {
        binding.apply {
            vFocus.isVisible = item.isSelected
            if (position == 0) {
                lnlAddItem.visible()
                imvImage.gone()
                lnlAddItem.tap(700) { onAddImageClick.invoke() }
            } else {
                lnlAddItem.gone()
                imvImage.visible()
                // Load image with 8dp rounded corners
                val cornerRadiusPx = (2 * root.context.resources.displayMetrics.density).toInt()
                Glide.with(root)
                    .load(item.path)
                    .override(256, 256)
                    .encodeQuality(60)
                    .transform(RoundedCorners(cornerRadiusPx))
                    .into(imvImage)
                imvImage.tap { onBackgroundImageClick.invoke(item.path, position) }
            }
        }
    }

    fun submitItem(position: Int, list: ArrayList<SelectedModel>){
        if (position != currentSelected){
            items.clear()
            items.addAll(list)

            notifyItemChanged(currentSelected)
            notifyItemChanged(position)

            currentSelected = position
        }
    }
}