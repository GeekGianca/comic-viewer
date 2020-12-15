package com.gksoftwaresolutions.comicviewer.view.home.ui.home

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gksoftwaresolutions.comicviewer.ComicApp
import com.gksoftwaresolutions.comicviewer.R
import com.gksoftwaresolutions.comicviewer.component.PagedListGroup
import com.gksoftwaresolutions.comicviewer.component.adapter.*
import com.gksoftwaresolutions.comicviewer.data.local.entities.ItemMarvel
import com.gksoftwaresolutions.comicviewer.databinding.FragmentHomeBinding
import com.gksoftwaresolutions.comicviewer.model.Comic
import com.gksoftwaresolutions.comicviewer.model.NetworkState
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import java.lang.Exception
import javax.inject.Inject
import kotlin.random.Random

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val adapter = GroupAdapter<GroupieViewHolder>()

    private val listItem = mutableListOf<Item<*>>()
    private val listCharacter = mutableListOf<Item<*>>()
    private val listCreator = mutableListOf<Item<*>>()
    private val listEvent = mutableListOf<Item<*>>()
    private val listSeries = mutableListOf<Item<*>>()

    private lateinit var adapterItems: AdapterGroupItemView
    private lateinit var adapterCharacter: AdapterGroupCharacterView
    private lateinit var adapterCreator: AdapterGroupCharacterView
    private lateinit var adapterEvent: AdapterGroupEventView
    private lateinit var adapterSeries: AdapterGroupSeriesView

    private val pagedList = PagedListGroup<AdapterItemView>()
    private val pagedCharacterList = PagedListGroup<AdapterCharacterView>()
    private val pagedCreatorList = PagedListGroup<AdapterCharacterView>()
    private val pagedEventList = PagedListGroup<AdapterEventView>()
    private val pagedSeriesList = PagedListGroup<AdapterSeriesView>()

    private var comicRandom: Comic? = null

    @Inject
    lateinit var mViewModel: HomeViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as ComicApp).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdaptersList()
        binding.let {
            it.groupieItems.layoutManager =
                LinearLayoutManager(it.root.context, RecyclerView.VERTICAL, false)
            it.groupieItems.adapter = adapter
        }
        binding.stories.setOnClickListener {
            if (comicRandom != null) {
                try {
                    if (comicRandom!!.urls.isNotEmpty()) {
                        startWebIntent(comicRandom!!.urls[0].url)
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        "The event contains no information",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        binding.favorite.setOnClickListener {
            if (binding.favorite.isChecked) {
                if (comicRandom != null) {
                    val url = if (comicRandom!!.urls.isNotEmpty()) {
                        comicRandom!!.urls[0].url
                    } else {
                        ""
                    }
                    mViewModel.addFavorites(
                        ItemMarvel(
                            comicRandom!!.id,
                            comicRandom!!.title,
                            comicRandom!!.description,
                            "Comics",
                            url,
                            "${comicRandom!!.thumbnail.path}.${comicRandom!!.thumbnail.extension}"
                        )
                    )
                }
            } else {
                if (comicRandom != null) {
                    mViewModel.removeFavorites(comicRandom!!.id)
                }
            }
        }
    }

    private fun startWebIntent(url: String) {
        val builder = CustomTabsIntent.Builder()
        builder.setStartAnimations(
            requireContext(),
            android.R.anim.fade_in,
            android.R.anim.fade_out
        );
        builder.setExitAnimations(
            requireContext(),
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right
        );
        builder.setDefaultColorSchemeParams(
            CustomTabColorSchemeParams
                .Builder()
                .setToolbarColor(ContextCompat.getColor(requireContext(), R.color.black))
                .setNavigationBarColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimaryDark
                    )
                )
                .build()
        )
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(url))
    }

    private fun setAdaptersList() {
        adapterItems = AdapterGroupItemView("Comics", pagedList)
        listItem.add(adapterItems)
        adapterCharacter = AdapterGroupCharacterView("Characters", pagedCharacterList)
        listCharacter.add(adapterCharacter)
        adapterCreator = AdapterGroupCharacterView("Creators", pagedCreatorList)
        listCreator.add(adapterCreator)
        adapterEvent = AdapterGroupEventView("Events", pagedEventList)
        listEvent.add(adapterEvent)
        adapterSeries = AdapterGroupSeriesView("Series", pagedSeriesList)
        listSeries.add(adapterSeries)
        adapter.addAll(listItem)
        adapter.addAll(listCharacter)
        adapter.addAll(listCreator)
        adapter.addAll(listEvent)
        adapter.addAll(listSeries)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        attachObservables()
    }

    private fun attachObservables() {
        mViewModel.comicObservableList.observe(viewLifecycleOwner, {
            pagedList.submitList(it)
        })
        mViewModel.charactersObservableList.observe(viewLifecycleOwner, {
            pagedCharacterList.submitList(it)
        })
        mViewModel.creatorObservableList.observe(viewLifecycleOwner, {
            pagedCreatorList.submitList(it)
        })
        mViewModel.eventsObservableList.observe(viewLifecycleOwner, {
            pagedEventList.submitList(it)
        })
        mViewModel.seriesObservableList.observe(viewLifecycleOwner, {
            pagedSeriesList.submitList(it)
        })

        mViewModel.observableSuccess().observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })

        mViewModel.observableSuccessRemove().observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })

        mViewModel.observableError().observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })

        mViewModel.observableComicRandom().observe(viewLifecycleOwner, {
            if (it != null) {
                comicRandom = it
                binding.recommendLayout.visibility = View.VISIBLE
                binding.backgroundHomeImage.setImageURI("${it.thumbnail.path}.${it.thumbnail.extension}")
                binding.homeImage.setImageURI("${it.thumbnail.path}.${it.thumbnail.extension}")
                binding.titleRecommend.text = it.title
                binding.descriptionRecommend.text = it.description
            } else {
                binding.recommendLayout.visibility = View.GONE
            }
        })

        mViewModel.networkCharacterObservable.observe(viewLifecycleOwner, {
            when {
                it === NetworkState.EMPTY -> {
                    adapterCharacter.hideLoading()
                    Toast.makeText(context, it.msg, Toast.LENGTH_LONG).show()
                }
                it === NetworkState.ENDOFLIST -> {
                    adapterCharacter.hideLoading()
                }
                it === NetworkState.LOADED -> {
                    adapterCharacter.hideLoading()
                }
                it === NetworkState.LOADING -> {
                    adapterCharacter.showLoading()
                }
                it === NetworkState.ERROR -> {
                    adapterCharacter.hideLoading()
                }
            }
        })

        mViewModel.networkObservable.observe(viewLifecycleOwner, {
            when {
                it === NetworkState.EMPTY -> {
                    adapterItems.hideLoading()
                    Toast.makeText(context, it.msg, Toast.LENGTH_LONG).show()
                }
                it === NetworkState.ENDOFLIST -> {
                    adapterItems.hideLoading()
                }
                it === NetworkState.LOADED -> {
                    adapterItems.hideLoading()
                }
                it === NetworkState.LOADING -> {
                    adapterItems.showLoading()
                }
                it === NetworkState.ERROR -> {
                    adapterItems.hideLoading()
                }
            }
        })

        mViewModel.networkCreatorObservable.observe(viewLifecycleOwner, {
            when {
                it === NetworkState.EMPTY -> {
                    adapterCreator.hideLoading()
                    Toast.makeText(context, it.msg, Toast.LENGTH_LONG).show()
                }
                it === NetworkState.ENDOFLIST -> {
                    adapterCreator.hideLoading()
                }
                it === NetworkState.LOADED -> {
                    adapterCreator.hideLoading()
                }
                it === NetworkState.LOADING -> {
                    adapterCreator.showLoading()
                }
                it === NetworkState.ERROR -> {
                    adapterCreator.hideLoading()
                }
            }
        })

        mViewModel.networkEventObservable.observe(viewLifecycleOwner, {
            when {
                it === NetworkState.EMPTY -> {
                    adapterEvent.hideLoading()
                    Toast.makeText(context, it.msg, Toast.LENGTH_LONG).show()
                }
                it === NetworkState.ENDOFLIST -> {
                    adapterEvent.hideLoading()
                }
                it === NetworkState.LOADED -> {
                    adapterEvent.hideLoading()
                }
                it === NetworkState.LOADING -> {
                    adapterEvent.showLoading()
                }
                it === NetworkState.ERROR -> {
                    adapterEvent.hideLoading()
                }
            }
        })

        mViewModel.networkSeriesObservable.observe(viewLifecycleOwner, {
            when {
                it === NetworkState.EMPTY -> {
                    adapterSeries.hideLoading()
                    Toast.makeText(context, it.msg, Toast.LENGTH_LONG).show()
                }
                it === NetworkState.ENDOFLIST -> {
                    adapterSeries.hideLoading()
                }
                it === NetworkState.LOADED -> {
                    adapterSeries.hideLoading()
                }
                it === NetworkState.LOADING -> {
                    adapterSeries.showLoading()
                }
                it === NetworkState.ERROR -> {
                    adapterSeries.hideLoading()
                }
            }
        })

        mViewModel.observableItem().observe(viewLifecycleOwner, {
            binding.favorite.isChecked = it != null
        })
    }

    override fun onResume() {
        super.onResume()
        val random = Random.nextInt(100, 8000)
        mViewModel.findRandomComic(random)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}