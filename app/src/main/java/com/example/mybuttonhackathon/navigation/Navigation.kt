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

package com.example.mybuttonhackathon.navigation

import androidx.annotation.MainThread
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.mybuttonhackathon.scenes.a.ScreenA
import com.example.mybuttonhackathon.scenes.b.ScreenB

enum class SceneName { SCENE_A, SCENE_B }

typealias SceneNavigation = (Scene) -> Unit

sealed class Scene(val id: SceneName) {
    @Composable
    abstract fun contentView(navigate: SceneNavigation)
}

object SceneA : Scene(SceneName.SCENE_A) {
    @Composable
    override fun contentView(navigate: SceneNavigation) = ScreenA(
        navigation = navigate::invoke
    )
}

data class SceneB(val value: String) : Scene(SceneName.SCENE_B) {
    @Composable
    override fun contentView(navigate: SceneNavigation) = ScreenB(value = value)
}

class NavigationViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    val current: MutableState<Scene> = mutableStateOf(SceneA)

    @MainThread
    fun onBack(): Boolean {
        val wasHandled = current.value != SceneA
        current.value = SceneA
        return wasHandled
    }

    @MainThread
    fun navigateTo(scene: Scene) {
        current.value = scene
    }
}
