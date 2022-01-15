package com.jxd.mapdemo.ui.theme

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ThemeViewModel : ViewModel() {
    private val _currentTheme = MutableStateFlow(value = ThemeModel.AUTO)
    val currentTheme: StateFlow<ThemeModel> = _currentTheme

    fun currentTheme(themeModel: ThemeModel) {
        _currentTheme.value = themeModel
    }
}


enum class ThemeModel {
    AUTO, LIGHT, DARK
}