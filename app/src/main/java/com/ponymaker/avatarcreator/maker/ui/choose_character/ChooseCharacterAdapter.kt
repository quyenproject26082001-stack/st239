package com.ponymaker.avatarcreator.maker.ui.choose_character

import com.ponymaker.avatarcreator.maker.core.base.BaseAdapter
import com.ponymaker.avatarcreator.maker.core.extensions.gone
import com.ponymaker.avatarcreator.maker.core.extensions.loadImage
import com.ponymaker.avatarcreator.maker.core.extensions.tap
import com.ponymaker.avatarcreator.maker.data.model.custom.CustomizeModel
import com.ponymaker.avatarcreator.maker.databinding.ItemChooseAvatarBinding

class ChooseCharacterAdapter : BaseAdapter<CustomizeModel, ItemChooseAvatarBinding>(ItemChooseAvatarBinding::inflate) {
    var onItemClick: ((position: Int) -> Unit) = {}
    override fun onBind(binding: ItemChooseAvatarBinding, item: CustomizeModel, position: Int) {
        binding.apply {
            loadImage(item.avatar, imvImage, onDismissLoading = {
                sflShimmer.stopShimmer()
                sflShimmer.gone()
            })
            root.tap { onItemClick.invoke(position) }
        }
    }
}