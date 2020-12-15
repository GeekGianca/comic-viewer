package com.gksoftwaresolutions.comicviewer.view.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.gksoftwaresolutions.comicviewer.ComicApp
import com.gksoftwaresolutions.comicviewer.R
import com.gksoftwaresolutions.comicviewer.component.adapter.AdapterCharacterView
import com.gksoftwaresolutions.comicviewer.component.adapter.AdapterEventView
import com.gksoftwaresolutions.comicviewer.component.adapter.AdapterItemView
import com.gksoftwaresolutions.comicviewer.component.adapter.AdapterSeriesView
import com.gksoftwaresolutions.comicviewer.data.local.entities.ItemMarvel
import com.gksoftwaresolutions.comicviewer.databinding.ActivityDetailBinding
import com.gksoftwaresolutions.comicviewer.model.Url
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import javax.inject.Inject

class DetailActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "DetailActivity"
    }

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!

    private val adapter = GroupAdapter<GroupieViewHolder>()
    private val itemsList = mutableListOf<Item<*>>()
    private val itemsComicsList = mutableListOf<Item<*>>()

    private var type = ""
    private var id = 0
    private var itemMarvel: ItemMarvel? = null

    @Inject
    lateinit var mViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        (applicationContext as ComicApp).appComponent.inject(this)
        setContentView(binding.root)
        if (intent.extras != null) {
            type = intent!!.extras!!.getString("type", "")
            id = intent!!.extras!!.getInt("findId", 0)
            when (type) {
                "Comics" -> {
                    mViewModel.findComic(id)
                    mViewModel.findCharactersByComic(id)
                    mViewModel.findItem(id)
                }
                "Characters" -> {
                    mViewModel.findCharacter(id)
                    mViewModel.findComicsByCharacters(id)
                    mViewModel.findItem(id)
                }
                "Creators" -> {
                    mViewModel.findCreator(id)
                    mViewModel.findComicsByCreator(id)
                    mViewModel.findItem(id)
                }
                "Events" -> {
                    mViewModel.findEvent(id)
                    mViewModel.findComicsByEvent(id)
                    mViewModel.findItem(id)
                }
                "Series" -> {
                    mViewModel.findSeries(id)
                    mViewModel.findCharactersBySeries(id)
                    mViewModel.findItem(id)
                }
                else -> finish()
            }
        }
        setListeners()
        setObservables()
    }

    private fun setListeners() {
        binding.let {
            it.content.displayDetail.layoutManager = GridLayoutManager(this@DetailActivity, 2)
            it.content.displayDetail.adapter = adapter
            adapter.setOnItemClickListener { item, _ ->
                when (item) {
                    is AdapterItemView -> {
                        startDetail(item.id, item.title)
                    }
                    is AdapterCharacterView -> {
                        startDetail(item.id, item.title)
                    }
                    is AdapterEventView -> {
                        startDetail(item.id, "Events")
                    }
                    is AdapterSeriesView -> {
                        startDetail(item.id, "Series")
                    }
                }
            }
            it.back.setOnClickListener {
                finish()
            }
        }

        binding.content.addFavorite.setOnClickListener {
            if (binding.content.addFavorite.isChecked) {
                if (itemMarvel != null)
                    mViewModel.addFavorites(itemMarvel!!)
            } else {
                mViewModel.removeFavorites(itemMarvel!!.id)
            }
        }

        binding.content.relatedPage.setOnClickListener {
            if (itemMarvel != null) {
                startWebIntent(itemMarvel!!.urlInfo)
            } else {
                Toast.makeText(this, "The $type contains no information", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun startWebIntent(url: String) {
        val builder = CustomTabsIntent.Builder()
        builder.setStartAnimations(
            this,
            android.R.anim.fade_in,
            android.R.anim.fade_out
        );
        builder.setExitAnimations(
            this,
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right
        );
        builder.setDefaultColorSchemeParams(
            CustomTabColorSchemeParams
                .Builder()
                .setToolbarColor(ContextCompat.getColor(this, R.color.black))
                .setNavigationBarColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorPrimaryDark
                    )
                )
                .build()
        )
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }

    private fun startDetail(id: Int, title: String) {
        val intent = Intent(this@DetailActivity, DetailActivity::class.java)
        intent.putExtra("findId", id)
        intent.putExtra("type", title)
        startActivity(intent)
    }

    private fun setObservables() {
        mViewModel.observableCharacter().observe(this, {
            setItemMarvel(
                it.id,
                it.name,
                it.description,
                "Characters",
                "${it.thumbnail.path}.${it.thumbnail.extension}",
                it.urls
            )

            setView(
                it.name,
                it.description,
                "${it.thumbnail.path}.${it.thumbnail.extension}",
                ""
            )
        })

        mViewModel.observableComic().observe(this, {
            setItemMarvel(
                it.id,
                it.title,
                it.description,
                "Comics",
                "${it.thumbnail.path}.${it.thumbnail.extension}",
                it.urls
            )
            setView(
                it.title,
                it.description,
                "${it.thumbnail.path}.${it.thumbnail.extension}",
                it.format
            )
        })

        mViewModel.observableCreator().observe(this, {
            setItemMarvel(
                it.id,
                it.fullName,
                "",
                "Creators",
                "${it.thumbnail.path}.${it.thumbnail.extension}",
                it.urls
            )
            setView(it.fullName, "", "${it.thumbnail.path}.${it.thumbnail.extension}", "")
        })

        mViewModel.observableError().observe(this, {
            MaterialAlertDialogBuilder(this)
                .setMessage(it)
                .setPositiveButton("Accept") { dialog, _ ->
                    dialog.dismiss()
                    dialog.cancel()
                }.create().show()
        })

        mViewModel.observableEvent().observe(this, {
            setItemMarvel(
                it.id,
                it.title,
                it.description,
                "Events",
                "${it.thumbnail.path}.${it.thumbnail.extension}",
                it.urls
            )
            setView(it.title, it.description, "${it.thumbnail.path}.${it.thumbnail.extension}", "")
        })

        mViewModel.observableSeries().observe(this, {
            setItemMarvel(
                it.id,
                it.title,
                it.description,
                "Series",
                "${it.thumbnail.path}.${it.thumbnail.extension}",
                it.urls
            )
            setView(it.title, it.description, "${it.thumbnail.path}.${it.thumbnail.extension}", "")
        })

        mViewModel.observableListCharacters().observe(this, { characters ->
            binding.content.title.text = getString(R.string.text_characters)
            characters.forEach {
                itemsList.add(
                    AdapterCharacterView(
                        it.id,
                        it.name,
                        "${it.thumbnail.path}.${it.thumbnail.extension}"
                    )
                )
            }
            adapter.addAll(itemsList)
            adapter.notifyDataSetChanged()
            if (itemsList.isEmpty()) {
                binding.content.emptyFound.text = String.format(
                    "Not found %s for this %s",
                    getString(R.string.text_characters),
                    type
                )
                binding.content.emptyFound.visibility = View.VISIBLE
            } else {
                binding.content.emptyFound.visibility = View.GONE
            }
        })

        mViewModel.observableListComics().observe(this, { comics ->
            binding.content.title.text = getString(R.string.text_comics)
            comics.forEach {
                itemsComicsList.add(
                    AdapterItemView(
                        it.id,
                        it.format,
                        it.title,
                        "${it.thumbnail.path}.${it.thumbnail.extension}"
                    )
                )
            }
            adapter.addAll(itemsComicsList)
            adapter.notifyDataSetChanged()
            if (itemsComicsList.isEmpty()) {
                binding.content.emptyFound.text = String.format(
                    "Not found %s for this %s",
                    getString(R.string.text_comics),
                    type
                )
                binding.content.emptyFound.visibility = View.VISIBLE
            } else {
                binding.content.emptyFound.visibility = View.GONE
            }
        })

        mViewModel.observableAddFavorite().observe(this, {
            if (it != null) {
                binding.content.addFavorite.isChecked = true
                binding.content.addFavorite.text = "Remove favorites"
            } else {
                binding.content.addFavorite.isChecked = false
                binding.content.addFavorite.text = "Add favorites"
            }
        })

        mViewModel.observableRemoveFavorite().observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun setItemMarvel(
        id: Int,
        name: String,
        description: String,
        type: String,
        urlImage: String,
        urls: List<Url>
    ) {
        val url = if (urls.isNotEmpty()) {
            urls[0].url
        } else {
            ""
        }
        itemMarvel = ItemMarvel(id, name, description ?: "", type, url, urlImage)
    }


    private fun setView(title: String, description: String, url: String, format: String) {
        binding.content.titleItem.text = title
        binding.content.description.text = description
        binding.imgBackground.setImageURI(url)
        binding.imgComics.setImageURI(url)
        binding.typeDesc.text = format
        if (format.isBlank()) {
            binding.layoutDesc.visibility = View.GONE
        } else {
            binding.layoutDesc.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}