package com.gksoftwaresolutions.comicviewer.view.home.ui.favorite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.gksoftwaresolutions.comicviewer.ComicApp
import com.gksoftwaresolutions.comicviewer.component.adapter.AdapterItemView
import com.gksoftwaresolutions.comicviewer.databinding.FavoriteFragmentBinding
import com.gksoftwaresolutions.comicviewer.util.Common
import com.gksoftwaresolutions.comicviewer.view.detail.DetailActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import javax.inject.Inject

class FavoriteFragment : Fragment() {

    private var _binding: FavoriteFragmentBinding? = null
    private val binding get() = _binding!!

    private val groupieAdapter = GroupAdapter<GroupieViewHolder>()
    private val items = mutableListOf<AdapterItemView>()

    @Inject
    lateinit var viewModel: FavoriteViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as ComicApp).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoriteFragmentBinding.inflate(inflater, container, false)
        groupieAdapter.spanCount = 2
        binding.favorites.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = groupieAdapter
            groupieAdapter.setOnItemClickListener { item, _ ->
                when (item) {
                    is AdapterItemView -> {
                        val intent = Intent(context, DetailActivity::class.java)
                        intent.putExtra("findId", item.id)
                        intent.putExtra("type", item.title)
                        requireContext().startActivity(intent)
                    }
                }
            }
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.observableAllItems().observe(viewLifecycleOwner, { marvelItems ->
            marvelItems.forEach {
                items.add(
                    AdapterItemView(
                        it.id,
                        it.type,
                        it.title,
                        it.urlImage
                    )
                )
            }
            groupieAdapter.addAll(items)
            groupieAdapter.notifyDataSetChanged()
            if (items.isNullOrEmpty()) {
                binding.empty.visibility = View.VISIBLE
            } else {
                binding.empty.visibility = View.GONE
            }
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.findAllItems()
        Common.INSTANCE = this
    }

}