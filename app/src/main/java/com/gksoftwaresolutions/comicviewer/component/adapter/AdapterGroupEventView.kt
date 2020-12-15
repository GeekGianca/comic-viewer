package com.gksoftwaresolutions.comicviewer.component.adapter

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gksoftwaresolutions.comicviewer.R
import com.gksoftwaresolutions.comicviewer.component.PagedListGroup
import com.gksoftwaresolutions.comicviewer.databinding.ItemGroupLayoutBinding
import com.gksoftwaresolutions.comicviewer.view.detail.DetailActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.viewbinding.BindableItem

class AdapterGroupEventView(
    val title: String,
    private val items: PagedListGroup<AdapterEventView>
) : BindableItem<ItemGroupLayoutBinding>() {

    private val groupieAdapter = GroupAdapter<GroupieViewHolder>()
    private var binding: ItemGroupLayoutBinding? = null

    override fun getLayout(): Int = R.layout.item_group_layout

    override fun initializeViewBinding(view: View): ItemGroupLayoutBinding =
        ItemGroupLayoutBinding.bind(view)

    override fun bind(viewBinding: ItemGroupLayoutBinding, position: Int) {
        binding = viewBinding
        groupieAdapter.add(items)
        viewBinding.title.text = title
        viewBinding.let {
            it.items.layoutManager =
                LinearLayoutManager(it.root.context, RecyclerView.HORIZONTAL, false)
            it.items.adapter = groupieAdapter
            groupieAdapter.setOnItemClickListener { item, _ ->
                when (item) {
                    is AdapterEventView -> {
                        val intent = Intent(it.root.context, DetailActivity::class.java)
                        intent.putExtra("findId", item.id)
                        intent.putExtra("type", title)
                        it.root.context.startActivity(intent)
                    }
                }
            }
        }
    }

    fun hideLoading() {
        if (binding != null)
            binding!!.loading.visibility = View.GONE
    }

    fun showLoading() {
        if (binding != null)
            binding!!.loading.visibility = View.VISIBLE
    }
}