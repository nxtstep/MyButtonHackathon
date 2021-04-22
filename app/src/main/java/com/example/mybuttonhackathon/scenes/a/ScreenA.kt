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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mybuttonhackathon.DismissableErrorDialog
import com.example.mybuttonhackathon.defaultSpacing
import com.example.mybuttonhackathon.navigation.SceneB
import com.example.mybuttonhackathon.navigation.SceneNavigation
import com.example.mybuttonhackathon.state.component1
import com.example.mybuttonhackathon.state.component2
import com.example.mybuttonhackathon.state.component3

@Composable
fun ScreenA(
    navigation: SceneNavigation,
    modifier: Modifier = Modifier,
    viewModelA: ClickViewModel = viewModel()
) {
    val (state, clearDataAction, dismissErrorAction) = viewModelA.state.collectAsState()

    Column(
        modifier = modifier
            .padding(horizontal = defaultSpacing, vertical = defaultSpacing)
            .fillMaxHeight()
    ) {
        Text(text = "Scene A", modifier = modifier)

        Spacer(
            modifier = Modifier
                .height(defaultSpacing)
                .then(modifier)
        )

        state.exception?.run {
            DismissableErrorDialog(error = this) {
                dismissErrorAction()
            }
        }

        state.data?.let {
            clearDataAction()
            navigation(SceneB(it))
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .then(modifier),
            onClick = viewModelA::sendClick,
            enabled = !state.loading
        ) {
            Text(text = "Click me")
        }
    }
}
