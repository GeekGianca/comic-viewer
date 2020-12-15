package com.gksoftwaresolutions.comicviewer.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.gksoftwaresolutions.comicviewer.ComicApp
import com.gksoftwaresolutions.comicviewer.R
import com.gksoftwaresolutions.comicviewer.databinding.ActivityMainBinding
import com.gksoftwaresolutions.comicviewer.view.home.HomeActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!


    @Inject
    lateinit var mViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        //Inject ViewModel in ViewModelFactory
        (applicationContext as ComicApp).appComponent.inject(this)
        setContentView(binding.root)
        setObservables()
        setListeners()
    }

    private fun setListeners() {
        binding.login.setOnClickListener {
            //fake validation inputs
            mViewModel.login(
                binding.usernameInput.text.toString(),
                binding.passwordInput.text.toString()
            )
        }
    }

    private fun setObservables() {
        lifecycleScope.launchWhenStarted {
            mViewModel.loginUiState.observe(this@MainActivity, {
                when (it) {
                    is MainViewModel.LoginUiState.Success -> {
                        startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                        finish()
                    }
                    is MainViewModel.LoginUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.errorAccessText.visibility = View.GONE
                    }
                    is MainViewModel.LoginUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorAccessText.visibility = View.VISIBLE
                        binding.errorAccessText.text = it.message
                    }
                    else -> Unit
                }
            })
        }
    }

    override fun onStart() {
        super.onStart()
        mViewModel.setParamsAuth(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}