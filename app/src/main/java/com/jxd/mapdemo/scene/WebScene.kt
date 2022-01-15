package com.jxd.mapdemo.scene

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jxd.mapdemo.widget.WebComponent

@Composable
fun WebScene() {
    Scaffold(modifier = Modifier.fillMaxSize()) {
        WebComponent(url = "https://test.housesigma.com/static/dark_test.html")
    }
}