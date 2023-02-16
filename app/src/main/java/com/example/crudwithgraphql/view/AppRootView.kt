package com.example.crudwithgraphql.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.crudwithgraphql.MainActivityViewModel

@Composable
fun AppRootView(
    modifier: Modifier = Modifier,
    viewModel: MainActivityViewModel
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "App")
    }
}