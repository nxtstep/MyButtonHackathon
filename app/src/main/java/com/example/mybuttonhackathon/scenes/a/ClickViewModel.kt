/*
 * MIT License
 *
 * Copyright (c) 2021 SuperSimple.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.example.mybuttonhackathon.scenes.a

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybuttonhackathon.feature.click.ClickRepository
import com.example.mybuttonhackathon.state.DismissableUiState
import com.example.mybuttonhackathon.state.UiState
import com.example.mybuttonhackathon.util.getValue
import com.example.mybuttonhackathon.util.setValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

@HiltViewModel
class ClickViewModel @Inject constructor(private val clickRepo: ClickRepository) : ViewModel() {
    private val clickChannel = Channel<Unit>(Channel.CONFLATED)
    private val state_ = MutableStateFlow(UiState<String>())
    private var uiState by state_

    val state: StateFlow<DismissableUiState<String>> = state_.mapLatest { newState ->
        DismissableUiState(
            state = newState,
            dismissData = { uiState = newState.copy(data = null) },
            dismissError = { uiState = newState.copy(exception = null) }
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, DismissableUiState(uiState))

    init {
        startObservingClickChannel()
    }

    fun sendClick() {
        clickChannel.offer(Unit)
    }

    private fun startObservingClickChannel() {
        viewModelScope.launch {
            supervisorScope {
                for (event in clickChannel) {
                    runCatching {
                        uiState = uiState.copy(loading = true)
                        clickRepo.click()
                    }.onSuccess {
                        uiState = uiState.copy(loading = false, data = it, exception = null)
                    }.onFailure {
                        uiState = uiState.copy(loading = false, exception = it)
                    }
                }
            }
        }
    }
}
