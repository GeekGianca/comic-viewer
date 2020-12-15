package com.gksoftwaresolutions.comicviewer.component.adapter

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.gksoftwaresolutions.comicviewer.R
import com.gksoftwaresolutions.comicviewer.databinding.ItemEventLayoutBinding
import com.gksoftwaresolutions.comicviewer.model.Url
import com.xwray.groupie.viewbinding.BindableItem

class AdapterEventView(
    val id: Int,
    val urls: List<Url>,
    private val urlImage: String
) : BindableItem<ItemEventLayoutBinding>() {

    override fun bind(viewBinding: ItemEventLayoutBinding, position: Int) {
        val uri = Uri.parse(urlImage)
        viewBinding.let {
            it.imageEvent.setImageURI(uri)
            it.detail.setOnClickListener { _ ->
                try {
                    if (!urls.isNullOrEmpty() && urls[0].type == "detail") {
                        startWebIntent(it.root.context, urls[0].url)
                    } else {
                        Toast.makeText(
                            it.root.context,
                            "The event does not contain details",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        it.root.context,
                        "The event does not contain details",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            it.info.setOnClickListener { _ ->
                try {
                    if (!urls.isNullOrEmpty() && urls[1].type == "wiki") {
                        startWebIntent(it.root.context, urls[1].url)
                    } else {
                        Toast.makeText(
                            it.root.context,
                            "The event contains no information",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        it.root.context,
                        "The event contains no information",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun startWebIntent(it: Context, url: String) {
        val builder = CustomTabsIntent.Builder()
        builder.setStartAnimations(
            it,
            android.R.anim.fade_in,
            android.R.anim.fade_out
        );
        builder.setExitAnimations(
            it,
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right
        );
        builder.setDefaultColorSchemeParams(
            CustomTabColorSchemeParams
                .Builder()
                .setToolbarColor(ContextCompat.getColor(it, R.color.black))
                .setNavigationBarColor(ContextCompat.getColor(it, R.color.colorPrimaryDark))
                .build()
        )
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(it, Uri.parse(url))
    }

    override fun getLayout(): Int = R.layout.item_event_layout

    override fun initializeViewBinding(view: View): ItemEventLayoutBinding =
        ItemEventLayoutBinding.bind(view)

}