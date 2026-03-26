package com.ponymaker.avatarcreator.maker.ui.add_character.adapter

import com.ponymaker.avatarcreator.maker.core.base.BaseAdapter
import com.ponymaker.avatarcreator.maker.core.extensions.loadImage
import com.ponymaker.avatarcreator.maker.core.extensions.loadImageSticker
import com.ponymaker.avatarcreator.maker.core.extensions.tap
import com.ponymaker.avatarcreator.maker.data.model.SelectedModel
import com.ponymaker.avatarcreator.maker.databinding.ItemStickerBinding

class StickerAdapter : BaseAdapter<SelectedModel, ItemStickerBinding>(ItemStickerBinding::inflate) {
    var onItemClick : ((String) -> Unit) = {}
    override fun onBind(binding: ItemStickerBinding, item: SelectedModel, position: Int) {
        binding.apply {
            loadImageSticker(root, item.path, imvSticker)
            root.tap { onItemClick.invoke(item.path) }
        }
    }
}