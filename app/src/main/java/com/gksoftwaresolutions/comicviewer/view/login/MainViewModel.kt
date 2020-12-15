package com.gksoftwaresolutions.comicviewer.view.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gksoftwaresolutions.comicviewer.R
import com.gksoftwaresolutions.comicviewer.component.ViewModelFactoryModule
import com.gksoftwaresolutions.comicviewer.util.Common
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class MainViewModel @Inject constructor() : ViewModel() {
    companion object {
        private const val TAG = "MainViewModel"
    }

    private val _loginUiState = MutableLiveData<LoginUiState>(LoginUiState.Empty)
    val loginUiState: LiveData<LoginUiState> = _loginUiState

    fun login(username: String, password: String) = viewModelScope.launch {
        _loginUiState.value = LoginUiState.Loading
        delay(2000L)
        if (username == "comic-application-test" && password == "fake-password") {
            _loginUiState.value = LoginUiState.Success
        } else {
            _loginUiState.value = LoginUiState.Error("Wrong username or password")
        }
    }

    /**
     *
     */
    sealed class LoginUiState {
        object Success : LoginUiState()
        data class Error(val message: String) : LoginUiState()
        object Loading : LoginUiState()
        object Empty : LoginUiState()
    }

    fun setParamsAuth(context: Context) {
        Log.d(TAG, "Set Keys and params from query")
        val timeStamp = Random.nextInt(0, 100)
        val publicKey = context.getString(R.string.public_key)
        val privateKey = context.getString(R.string.private_key)
        val hash = Common.makeMd5HASH(timeStamp, privateKey, publicKey)
        Common.PARAMS["ts"] = "$timeStamp"
        Common.PARAMS["apikey"] = publicKey
        Common.PARAMS["hash"] = hash
        Common.CLEAN_PARAMS["ts"] = "$timeStamp"
        Common.CLEAN_PARAMS["apikey"] = publicKey
        Common.CLEAN_PARAMS["hash"] = hash
    }

    override fun onCleared() {
        super.onCleared()

    }
}