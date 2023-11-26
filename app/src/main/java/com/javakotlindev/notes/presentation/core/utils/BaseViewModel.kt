package com.javakotlindev.notes.presentation.core.utils

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

abstract class BaseViewModel<STATE : Any, EFFECT : Any>(initialState: STATE) :
    ViewModel(), CoroutineScope {

    private val errorHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
        handleError(error)
    }

    override val coroutineContext = SupervisorJob() + Dispatchers.Main.immediate + errorHandler

    // Get Current State
    val state: STATE
        get() = uiState.value

    private val _uiState: MutableStateFlow<STATE> = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _effect: Channel<EFFECT> = Channel()
    val sideEffect = _effect.receiveAsFlow()

    suspend fun postSideEffect(sideEffect: EFFECT) =
        coroutineScope { launchUI { _effect.send(sideEffect) } }

    suspend fun reduce(block: (state: STATE) -> STATE) = withUIContext {
        _uiState.value = block(uiState.value)
    }


    protected fun handleError(throwable: Throwable) {
        Log.e("BaseViewModel", "handleError: ", throwable)
    }

    override fun onCleared() {
        coroutineContext.cancel()
        super.onCleared()
    }
}
