package com.gksoftwaresolutions.comicviewer.component.adapter

import android.net.Uri
import android.view.View
import com.gksoftwaresolutions.comicviewer.R
import com.gksoftwaresolutions.comicviewer.databinding.ItemSeriesLayoutBinding
import com.xwray.groupie.viewbinding.BindableItem

class AdapterSeriesView(
    val id: Int,
    private val startYear: Int,
    private val endYear: Int,
    private val type: String,
    private val rate: String,
    private val url: String
) : BindableItem<ItemSeriesLayoutBinding>() {
    override fun bind(viewBinding: ItemSeriesLayoutBinding, position: Int) {
        viewBinding.let {
            it.endYear.text = "\u2022 End Year: $endYear"
            it.startYear.text = "\u2022 Start Year: $startYear"
            it.rating.text = "\u2022 Rating: $rate"
            it.type.text = "\u2022 Type: $type"
            it.image.setImageURI(url)
        }
    }

    override fun getLayout(): Int = R.layout.item_series_layout

    override fun initializeViewBinding(view: View): ItemSeriesLayoutBinding =
        ItemSeriesLayoutBinding.bind(view)
}