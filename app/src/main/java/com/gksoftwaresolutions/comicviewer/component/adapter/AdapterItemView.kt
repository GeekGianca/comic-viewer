package com.gksoftwaresolutions.comicviewer.component.adapter

import android.content.Intent
import android.view.View
import com.gksoftwaresolutions.comicviewer.R
import com.gksoftwaresolutions.comicviewer.databinding.ItemImageLayoutBinding
import com.gksoftwaresolutions.comicviewer.util.Common
import com.gksoftwaresolutions.comicviewer.view.detail.DetailActivity
import com.gksoftwaresolutions.comicviewer.view.home.ui.favorite.FavoriteFragment
import com.xwray.groupie.viewbinding.BindableItem

class AdapterItemView(
    val id: Int,
    private val format: String,
    val title: String,
    private val imageUrl: String
) :
    BindableItem<ItemImageLayoutBinding>() {
    override fun bind(viewBinding: ItemImageLayoutBinding, position: Int) {
        viewBinding.let {
            it.issueNumber.text = "#$id"
            it.imageItem.setImageURI(imageUrl)
        }
    }

    override fun getLayout(): Int = R.layout.item_image_layout

    override fun initializeViewBinding(view: View): ItemImageLayoutBinding =
        ItemImageLayoutBinding.bind(view)
}