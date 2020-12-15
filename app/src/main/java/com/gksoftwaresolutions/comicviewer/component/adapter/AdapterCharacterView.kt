package com.gksoftwaresolutions.comicviewer.component.adapter

import android.net.Uri
import android.view.View
import com.gksoftwaresolutions.comicviewer.R
import com.gksoftwaresolutions.comicviewer.databinding.ItemCharacterLayoutBinding
import com.xwray.groupie.viewbinding.BindableItem

class AdapterCharacterView(
    val id:Int,
    val title: String,
    private val url: String
) : BindableItem<ItemCharacterLayoutBinding>() {

    override fun bind(viewBinding: ItemCharacterLayoutBinding, position: Int) {
        val uri = Uri.parse(url)
        viewBinding.let {
            it.titleItem.text = title
            it.imageItem.setImageURI(uri)
        }
    }

    override fun getLayout(): Int = R.layout.item_character_layout

    override fun initializeViewBinding(view: View): ItemCharacterLayoutBinding =
        ItemCharacterLayoutBinding.bind(view)
}